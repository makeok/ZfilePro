// 文件分类
const fileTypeMap = {
    image: ['gif', 'jpg', 'jpeg', 'png', 'bmp', 'webp', 'ico'],
    video: ['mp4', 'webm', 'm3u8', 'rmvb', 'avi', 'swf', '3gp', 'mkv', 'flv'],
    audio: ['mp3', 'wav', 'wma', 'ogg', 'aac', 'flac', 'm4a'],
    text: ['scss', 'sass', 'kt', 'gitignore', 'bat', 'properties', 'yml', 'css', 'js', 'md', 'xml', 'txt', 'py', 'go', 'html', 'less', 'php', 'rb', 'rust', 'script', 'java', 'sh', 'sql'],
    executable: ['exe', 'dll', 'com', 'vbs'],
    archive: ['7z', 'zip', 'rar', 'tar', 'gz'],
    pdf: ['pdf'],
    office: ['doc', 'docx', 'csv', 'xls', 'xlsx', "ppt", 'pptx'],
    three3d: ['dae', 'fbx', 'gltf', 'glb', 'obj', 'ply', 'stl'],
    document: ['txt', 'pages', 'epub', 'numbers', 'keynote'],
};

// 可预览的文件类型
const previewFileType = ['image', 'video', 'audio', 'text', 'office', 'pdf', 'three3d'];

import config from '/package.json'

// 自动对 /src/assets/icons 目录下的文件图标进行显示
const iconFileType = [];
import ids from 'virtual:svg-icons-names'
ids.forEach(id => {
    iconFileType.push(id.replace(/^icon-file-type-/, ''));
});

let common = {
    responseCode: {
        SUCCESS: 0,
        FAIL: -1,
        REQUIRED_PASSWORD: 405,
        INVALID_PASSWORD: 406
    },
    storageType: {
        s3Type: ['s3', 'tencent', 'aliyun', 'qiniu', 'minio', 'huawei', 'doge-cloud'],
        proxyType: ['local', 'webdav', 'ftp', 'sftp', 'google-drive'],
        micrsoftType: ['sharepoint', 'sharepoint-china', 'onedrive', 'onedrive-china']
    },
    version: config.version,
    constant: {
        fileTypeMap,
        iconFileType,
        previewFileType
    },
    openPage: (url) => {
        window.open(url);
    },
    fileSizeFormat: (bytes) => {
        if (bytes === 0) return '-';
        if (bytes === -1) return '未知';
        let k = 1024;
        let sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
        let i = Math.floor(Math.log(bytes) / Math.log(k));
        return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i];
    },
    fileSizeFilter: (row, column, bytes) => {
        if (row.type === "BACK") return '';
        if (row.type === "FOLDER" && !row.size) return '-';
        if (bytes === 0) return '0 B';
        if (bytes === -1) return '未知';
        let k = 1024;
        let sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
        let i = Math.floor(Math.log(bytes) / Math.log(k));
        return common.fileSizeFormat(bytes);
    },
    getFileIconName(file) {
        let iconName;
        if (file.type === 'BACK' || file.type === 'FOLDER' || file.type === 'ROOT') {
            return file.type.toLowerCase();
        } else {
            let fileSuffix = this.getFileSuffix(file.name);
            let fileType = this.getFileType(file.name);

            if (iconFileType.indexOf(fileSuffix) !== -1) {
                iconName = fileSuffix;
            } else if (fileType) {
                iconName = fileType;
            } else {
                iconName = 'file';
            }
        }
        return iconName;
    },
    getFileSuffix(name) {
        let lastIndex = name.lastIndexOf('.');
        if (lastIndex === -1) {
            return 'other';
        }
        return name.substring(lastIndex + 1).toLowerCase();
    },
    getFileName(name) {
        let lastIndex = name.lastIndexOf('.');
        if (lastIndex === -1) {
            return '';
        }
        return name.substring(0, lastIndex);
    },
    getFileType(name) {
        let fileType;
        for (let key in fileTypeMap) {
            let suffix = this.getFileSuffix(name);
            if (fileTypeMap[key].indexOf(suffix) !== -1) {
                fileType = key;
                break;
            }
        }
        return fileType;
    },
    removeDuplicateSeparator(path) {
        let result = '';

        if (path.indexOf("http://") === 0) {
            result = "http://";
        } else if (path.indexOf("https://") === 0) {
            result = "https://";
        }

        for (let i = result.length; i < path.length - 1; i++) {
            let current = path.charAt(i);
            let next = path.charAt(i + 1);
            if (!(current === '/' && next === '/')) {
                result += current;
            }
        }
        result += path.charAt(path.length - 1);
        return result;
    },
    isMobile() {
        let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
        return flag || window.innerWidth < 768;
    },
    dateFormat:function(time) {
        if (!time) {
            return time;
        }
        let date = new Date(time);
        let year = date.getFullYear();
        let month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        let minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        let seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
    }
};

export default common;