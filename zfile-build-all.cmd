cd zfile-pro-vue && node --max_old_space_size=1024000 ./node_modules/vite/bin/vite.js build 

powershell -Command "(gc .\\dist\\zfile.config.json) -replace 'http://127.0.0.1:8080/', '' | Out-File -Encoding utf8 .\\dist\\zfile.config.json"

rd /s/q ..\\zfile-pro-main\\src\\main\\resources\\static && md ..\\zfile-pro-main\\src\\main\\resources\\static && xcopy /e/c/Y dist\\*.* ..\\zfile-pro-main\\src\\main\\resources\\static\\ && cd ../zfile-pro-main && D:/programs/idea/plugins/maven/lib/maven3/bin/mvn.cmd clean package  --settings D:/dev/maven/settings-alimaven.xml