# main.py
from fastapi import FastAPI, Form, Request
from fastapi.responses import HTMLResponse
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
import httpx
import os

app = FastAPI()
app.mount("/static", StaticFiles(directory="static"), name="static")
templates = Jinja2Templates(directory="templates")

# ប្តូរ API Key នៅទីនេះ
GROK_API_KEY = "grok-api-key-here"  # ឬ GPT, Gemini
GROK_API_URL = "https://api.x.ai/v1/chat/completions"

@app.get("/", response_class=HTMLResponse)
async def home(request: Request):
    return templates.TemplateResponse("index.html", {"request": request})

@app.post("/chat")
async def chat(message: str = Form(...)):
    headers = {
        "Authorization": f"Bearer {GROK_API_KEY}",
        "Content-Type": "application/json"
    }
    payload = {
        "messages": [
            {"role": "system", "content": "ឆ្លើយជាភាសាខ្មែរ និងសាមញ្ញ"},
            {"role": "user", "content": message}
        ],
        "model": "grok-beta",
        "temperature": 0.7
    }
    
    async with httpx.AsyncClient() as client:
        response = await client.post(GROK_API_URL, json=payload, headers=headers, timeout=30.0)
        if response.status_code == 200:
            reply = response.json()["choices"][0]["message"]["content"]
        else:
            reply = "មានបញ្ហា។ សាកល្បងម្តងទៀត។"
    
    return {"reply": reply}
