---
title: 'Security scan detected on {{ env.MODULE }}'
labels: [ 'security', 'dependencies', 'automated-scan' ]
name: 'Dependency Scan Report'
about: 'Auto-generated security scan report'
---

# 🔍 Dependency Scan Report - Vulnerabilities Found

- **Module:** {{ env.MODULE }}
- **Scan Date:** {{ env.SCAN_DATE }}
- **Vulnerabilities Found:** {{ env.VULN_COUNT }}

📊 **CVE Summary**
{{ env.CVES }}