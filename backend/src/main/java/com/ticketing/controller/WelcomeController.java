package com.ticketing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Welcome controller providing a nice HTML landing page at the root endpoint.
 */
@Controller
public class WelcomeController {

    @GetMapping("/")
    @ResponseBody
    public String welcome() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Event Ticketing API</title>
                    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600;700&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
                    <style>
                        * { margin: 0; padding: 0; box-sizing: border-box; }
                        body {
                            font-family: 'Inter', system-ui, sans-serif;
                            background: #0d0d0d;
                            color: #faf7f2;
                            min-height: 100vh;
                            display: flex;
                            flex-direction: column;
                            align-items: center;
                            justify-content: center;
                            padding: 40px 20px;
                        }
                        .container {
                            max-width: 600px;
                            text-align: center;
                        }
                        .logo {
                            font-size: 4rem;
                            margin-bottom: 16px;
                        }
                        h1 {
                            font-family: 'Playfair Display', Georgia, serif;
                            font-size: 2.5rem;
                            margin-bottom: 8px;
                            background: linear-gradient(135deg, #faf7f2 0%, #e5c76b 100%);
                            -webkit-background-clip: text;
                            -webkit-text-fill-color: transparent;
                            background-clip: text;
                        }
                        .subtitle {
                            color: #a39e93;
                            margin-bottom: 40px;
                        }
                        .status {
                            display: inline-flex;
                            align-items: center;
                            gap: 8px;
                            background: rgba(45, 106, 79, 0.2);
                            border: 1px solid rgba(45, 106, 79, 0.4);
                            padding: 8px 20px;
                            border-radius: 20px;
                            color: #4ade80;
                            font-weight: 500;
                            margin-bottom: 40px;
                        }
                        .status::before {
                            content: '';
                            width: 8px;
                            height: 8px;
                            background: #4ade80;
                            border-radius: 50%;
                            animation: pulse 2s infinite;
                        }
                        @keyframes pulse {
                            0%, 100% { opacity: 1; }
                            50% { opacity: 0.5; }
                        }
                        .card {
                            background: #1a1814;
                            border: 1px solid #3d3529;
                            border-radius: 12px;
                            padding: 24px;
                            margin-bottom: 24px;
                            text-align: left;
                        }
                        .card h2 {
                            font-family: 'Playfair Display', serif;
                            font-size: 1.25rem;
                            color: #e5c76b;
                            margin-bottom: 16px;
                        }
                        .endpoint {
                            display: flex;
                            gap: 12px;
                            padding: 10px 0;
                            border-bottom: 1px solid #3d3529;
                            font-size: 0.9rem;
                        }
                        .endpoint:last-child { border-bottom: none; }
                        .endpoint { text-decoration: none; transition: background 0.2s; }
                        .endpoint:hover { background: #252118; margin: 0 -12px; padding-left: 12px; padding-right: 12px; border-radius: 6px; }
                        .method {
                            background: #252118;
                            padding: 4px 10px;
                            border-radius: 4px;
                            font-weight: 600;
                            font-size: 0.75rem;
                            min-width: 50px;
                            text-align: center;
                        }
                        .method.get { color: #4ade80; }
                        .method.post { color: #60a5fa; }
                        .path { color: #a39e93; }
                        .cta {
                            display: inline-block;
                            background: #c9a227;
                            color: #0d0d0d;
                            padding: 14px 32px;
                            border-radius: 8px;
                            text-decoration: none;
                            font-weight: 600;
                            transition: all 0.2s;
                        }
                        .cta:hover {
                            background: #e5c76b;
                            transform: translateY(-2px);
                        }
                        .footer {
                            margin-top: 40px;
                            color: #6b6560;
                            font-size: 0.85rem;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="logo">🎭</div>
                        <h1>Event Ticketing API</h1>
                        <p class="subtitle">Spring Boot + PostgreSQL + JPA</p>

                        <div class="status">Server Running</div>

                        <div class="card">
                            <h2>Browse API Data</h2>
                            <a href="/view/events" class="endpoint">
                                <span class="method get">GET</span>
                                <span class="path">/view/events</span>
                            </a>
                            <a href="/view/events/1" class="endpoint">
                                <span class="method get">GET</span>
                                <span class="path">/view/events/1</span>
                            </a>
                            <a href="/view/events/1/seats" class="endpoint">
                                <span class="method get">GET</span>
                                <span class="path">/view/events/1/seats</span>
                            </a>
                        </div>

                        <a href="http://localhost:5173" class="cta">Open Frontend →</a>

                        <p class="footer">Full-Stack Event Ticketing System</p>
                    </div>
                </body>
                </html>
                """;
    }
}
