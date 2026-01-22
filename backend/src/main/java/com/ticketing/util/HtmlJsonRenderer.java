package com.ticketing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Utility to wrap JSON responses in styled HTML for browser viewing.
 */
public class HtmlJsonRenderer {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String render(String title, Object data) {
        String json;
        try {
            json = mapper.writeValueAsString(data);
        } catch (Exception e) {
            json = "Error serializing data";
        }

        // Escape HTML entities in JSON
        json = json.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>%s - Event Ticketing API</title>
                    <link href="https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
                    <style>
                        * { margin: 0; padding: 0; box-sizing: border-box; }
                        body {
                            font-family: 'Inter', system-ui, sans-serif;
                            background: #0d0d0d;
                            color: #faf7f2;
                            min-height: 100vh;
                            padding: 40px 24px;
                        }
                        .container {
                            max-width: 900px;
                            margin: 0 auto;
                        }
                        .header {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            margin-bottom: 24px;
                            padding-bottom: 24px;
                            border-bottom: 1px solid #3d3529;
                        }
                        h1 {
                            font-size: 1.5rem;
                            color: #e5c76b;
                        }
                        .back-link {
                            color: #a39e93;
                            text-decoration: none;
                            font-size: 0.9rem;
                            padding: 8px 16px;
                            border: 1px solid #3d3529;
                            border-radius: 6px;
                            transition: all 0.2s;
                        }
                        .back-link:hover {
                            background: #1a1814;
                            color: #faf7f2;
                        }
                        .json-container {
                            background: #1a1814;
                            border: 1px solid #3d3529;
                            border-radius: 12px;
                            padding: 24px;
                            overflow-x: auto;
                        }
                        pre {
                            font-family: 'JetBrains Mono', monospace;
                            font-size: 0.85rem;
                            line-height: 1.6;
                            color: #a39e93;
                        }
                        .string { color: #4ade80; }
                        .number { color: #60a5fa; }
                        .boolean { color: #f59e0b; }
                        .null { color: #6b6560; }
                        .key { color: #e5c76b; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>%s</h1>
                            <a href="/" class="back-link">← Back to API</a>
                        </div>
                        <div class="json-container">
                            <pre>%s</pre>
                        </div>
                    </div>
                    <script>
                        // Simple JSON syntax highlighting
                        const pre = document.querySelector('pre');
                        let html = pre.innerHTML;
                        html = html.replace(/"([^"]+)":/g, '<span class="key">"$1"</span>:');
                        html = html.replace(/: "([^"]*)"/g, ': <span class="string">"$1"</span>');
                        html = html.replace(/: (\\d+)/g, ': <span class="number">$1</span>');
                        html = html.replace(/: (true|false)/g, ': <span class="boolean">$1</span>');
                        html = html.replace(/: (null)/g, ': <span class="null">$1</span>');
                        pre.innerHTML = html;
                    </script>
                </body>
                </html>
                """
                .formatted(title, title, json);
    }
}
