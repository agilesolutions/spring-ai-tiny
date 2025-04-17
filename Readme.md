# Overview
Demo project to POC Agentic AI with Spring AI with tools built on GAMMA3 model running on google colab hosted by ollama server.
1. This solution fetches share prices from a public API ([twelvedata.com](https://support.twelvedata.com/)) and stores it on Azure AI Search to support RAG (Retrieval Augmented Generation).
2. [AI Agent](https://www.pega.com/agentic-ai?utm_source=google&utm_medium=cpc&utm_campaign=G_DACH_NonBrand_AgenticAI_CE_Exact_(CPN-111052)_EN&utm_term=agentic%20ai&gloc=9189123&utm_content=pcrid%7C731149222736%7Cpkw%7Ckwd-1490950831424%7Cpmt%7Ce%7Cpdv%7Cc%7C&gad_source=1&gclid=Cj0KCQjwkZm_BhDrARIsAAEbX1HB619ps6TWXRDu9QIvvLbQJ98faUewIqpHV6y2beDb_ayi1qLpbNoaAvxMEALw_wcB&gclsrc=aw.ds#p-c6e30b6a-0e50-4df1-bc14-357576efd817) functionality to letting the AI model autonomously perform tasks, like sending out messages without user intervention, depending on the inquiry and AI response. However, it’s important to differentiate between agentic AI and AI agents. Essentially, agentic AI is the framework; AI agents are the building blocks within the framework.
3. **NEW** : Introduce [Model Context Protocol](https://modelcontextprotocol.io/introduction) (MCP), introduce an agent and complex workflows on top of LLMs. Go beyound general knowledge-based question-answering pattern and enhance AI LLM agent connect to search engines, databases and file systems.

## Host GAMMA3 and Ollama Models on Google Colab
**Step 1:** Open your notebook at [https://colab.research.google.com/](https://colab.research.google.com/)

```
# Download and install ollama to the system
!curl -fsSL https://ollama.com/install.sh | sh 
```
**Step 2:** Install the Ollama Python SDK and pyngrok, the Python library that allows us to create secure tunnels
```
!pip install pyngrok ollama
```
**Step 3:** Start ollama server
```
import subprocess
def start_ollama_server() -> None:
    """Starts the Ollama server."""
    subprocess.Popen(['ollama', 'serve'])
    print("Ollama server started.")

start_ollama_server()
```
**Step 4:** Sign up for a [free ngrok](https://dashboard.ngrok.com/) account and get your authtoken. Store this authtoken securely in your Colab secrets (using the key icon on the left sidebar). Name the secret NGROK_AUTHTOKEN.
**Step 5:** Run this function to setting up the ngrok Tunnel, opening the secure door to your colab server. This will print out the full URL to remotely connect to ollama server running on colab.
```
from pyngrok import ngrok
from google.colab import userdata
def setup_ngrok_tunnel(port: str) -> ngrok.NgrokTunnel:
    ngrok_auth_token = userdata.get('NGROK_AUTHTOKEN')
    if not ngrok_auth_token:
        raise RuntimeError("NGROK_AUTHTOKEN is not set.")

    ngrok.set_auth_token(ngrok_auth_token)
    tunnel = ngrok.connect(port, host_header=f'localhost:{port}')
    print(f"ngrok tunnel created: {tunnel.public_url}")
    return tunnel
    
NGROK_PORT = '11434'
ngrok_tunnel = setup_ngrok_tunnel(NGROK_PORT)
```
**Step 5:** finally run your ollama model
```
!ollama run gemma3:1b
```
**Step 6:** run this springboot app and hit one of the REST API endpionts
```
http://localhost:80/agent/with-tools
```


