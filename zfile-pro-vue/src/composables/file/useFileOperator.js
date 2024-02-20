import {
    batchDeleteReq,
    newFolderReq,
    renameFileReq,
    renameFolderReq,
    unFolderReq,
    findRepeatFileReq,
    arrangeFolderByTimeReq,
    arrangeFolderByAddressReq,
    arrangeFolderByFileTypeReq,
} from "~/api/file-operator";

import useFileDataStore from "~/stores/file-data";
let fileDataStore = useFileDataStore();

import useStorageConfigStore from "~/stores/storage-config";
let storageConfigStore = useStorageConfigStore();

import useFileData from "~/composables/file/useFileData";
import useRouterData from "~/composables/useRouterData";
let { storageKey, currentPath } = useRouterData();

import useFileSelect from "~/composables/file/useFileSelect";
let { selectRows, selectRow, selectFolders, selectFiles } = useFileSelect();

import useFilePwd from "~/composables/file/useFilePwd";
let { getPathPwd } = useFilePwd();

// 检测浏览器类型
import uaBrowser from 'ua-browser'
import { ElLoading } from "element-plus";
const browserInfo = uaBrowser();

export default function useFileOperator() {

    const { loadFile } = useFileData();

    /**
     * 批量下载已选择的所有文件
     * @param {Object} row 已选择的文件
     */
    const batchDownloadFile = (row) => {
        if (!selectRows.value && selectRows.value.length === 0) {
            ElMessage.warning("请至少选择一个文件");
            return;
        }

        let confirmMsg;

        const checkBrowser = () => {
            let result = {
                isChrome: false,
                tips: ''
            }

            let currentBrowser = browserInfo.browser;
            let currentBrowserVersion = browserInfo.version;

            if (currentBrowser === 'Chrome' || navigator.userAgent.indexOf('Gecko')>0 || navigator.userAgent.indexOf('AppleWebKit')>0 || 
                    navigator.userAgent.indexOf('Presto')>0) {
                result.isChrome = true;
            } else {
                result.tips = `<br><span class="text-gray-500 text-xs">检测到当前浏览器为 <b>${currentBrowser}-${currentBrowserVersion}</b>, 可能不支持此功能，建议使用谷歌浏览器!</span>`;
            }
            return result;
        }

        const checkBrowserResult = checkBrowser();

        if (row?.name) {
            confirmMsg = `是否确认下载文件 <span class="text-blue-500">${row.name}</span> ？`;
        } else if (selectRows.value.length === 1) {
            confirmMsg = `是否确认下载文件 <span class="text-blue-500">${selectRows.value[0].name}</span> ？`;
            row = selectRows.value[0];
        } else if (selectRows.value.length > 1) {
            confirmMsg = `是否确认批量下载 ${selectRows.value.length} 个文件？`;
            if (!checkBrowserResult.isChrome) {
                confirmMsg += checkBrowserResult.tips;
            }
        }

        ElMessageBox.confirm(confirmMsg, '提示', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'info',
            callback: (action) => {
                if (action === 'confirm') {
                    // 单个文件下载, 直接下载
                    if (row?.name) {
                        console.log('进行指定文件下载, 文件:', row);
                        downloadFileUseWindowOpenMode(row.url)
                    } else {
                        // 批量下载
                        selectRows.value.forEach((item) => {
                            if (item.type === 'FILE') {
                                console.log('批量选中文件下载, 文件:', item);
                                downloadFileUseIframeMode(item.url);
                            }
                        })
                    }
                }
            }
        })
    }

    /**
     * 使用 windows.open 模式下载文件
     *
     * @param url   下载文件 url
     */
    const downloadFileUseWindowOpenMode = (url) => {
        window.open(url);
    }

    /**
     * 使用 iframe 模式下载文件
     *
     * @param url   下载文件 url
     */
    const downloadFileUseIframeMode = (url) => {
        const iframe = document.createElement("iframe");
        iframe.style.display = "none";  // 防止影响页面
        iframe.style.height = 0;  // 防止影响页面
        iframe.src = url;
        document.body.appendChild(iframe);
        setTimeout(()=>{
            iframe.remove();
        }, 5 * 60 * 1000);
    }

    // 新建文件夹
    const newFolder = () => {
        ElMessageBox.prompt(`在 <b>${currentPath.value}</b> 下创建文件夹，请输入要创建的文件夹名称`, '提示', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            draggable: true,
            inputValidator(val) {
                if (!val) {
                    return '文件夹名称不能为空';
                }

                if (val.includes("/")) {
                    return '文件夹名称不能包含 /';
                }
                return true;
            },
        }).then(({value}) => {
            let param = {
                storageKey: storageKey.value,
                path: currentPath.value,
                name: value
            }
            newFolderReq(param).then(() => {
                ElMessage.success('创建成功');
            }).finally(() => {
                loadFile();
            });
        });
    }

    // 重命名文件夹
    const rename = () => {
        let row = selectRow.value;
        if (row === null) {
            ElMessage.warning('请先选中一个文件或文件夹！');
            return;
        }
        ElMessageBox.prompt(`将 <b>${row.name}</b> 修改为：`, '提示', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputValue: row.name,
            inputValidator(val) {
                return !!val
            },
            inputErrorMessage: '模板名称不能为空.'
        }).then(({value}) => {
            let param = {
                storageKey: storageKey.value,
                path: row.path,
                name: row.name,
                newName: value,
            }

            let reqMethod;
            if (row.type === 'FILE') {
                reqMethod = renameFileReq;
            } else if (row.type === 'FOLDER') {
                reqMethod = renameFolderReq;
            }

            const renameLoadingInstance = ElLoading.service({
                fullscreen: true,
                text: '重命名中...',
                background: 'rgba(255, 255, 255, 0.6)'
            })

            reqMethod(param).then(() => {
                ElMessage.success('重命名成功');
            }).finally(() => {
                renameLoadingInstance.close();
                loadFile();
            });

        });
    }

    // 移动至重命名
    const moveTo = () => {
        ElMessage.warning("暂未实现");
    }

    // 复制至
    const copyTo = () => {
        ElMessage.warning("暂未实现");
    }



    // 删除相关 start
    const batchDelete = () => {
        if (!storageConfigStore.permission.delete) {
            return;
        }

        if (selectRows.value.length === 0) {
            ElMessage.warning('请先至少选中一个文件或文件夹！');
            return;
        }

        let deleteConfirmMsg = selectRows.value.length === 1 ? '是否确认删除 ' : '是否确认批量删除 ';

        let notSupportDeleteNotEmptyFolderType = ['s3', 'tencent', 'aliyun', 'qiniu', 'minio', 'huawei', 'upyun'];

        if (selectFolders.value.length > 0) {
            deleteConfirmMsg += (' ' + selectFolders.value.length + ' 个文件夹');
        }

        if (selectFolders.value.length > 0 && selectFiles.value.length > 0) {
            deleteConfirmMsg += '，';
        }

        if (selectFiles.value.length > 0) {
            deleteConfirmMsg += (selectFiles.value.length + ' 个文件');
        }

        if (selectFolders.value.length > 0 && notSupportDeleteNotEmptyFolderType.includes(fileDataStore.currentStorageSource.type.key)) {
            deleteConfirmMsg += (' (不支持删除非空文件夹)');
        }

        deleteConfirmMsg += "?"

        ElMessageBox.confirm(deleteConfirmMsg, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            draggable: true,
            callback: action => {
                if (action === 'confirm') {
                    let param = {
                        storageKey: storageKey.value,
                        deleteItems: []
                    };

                    selectRows.value.forEach((item) => {
                        param.deleteItems.push({
                            path: item.path,
                            name: item.name,
                            type: item.type,
                            password: getPathPwd(item.path)
                        });
                    })

                    // 打开全屏 loading
                    const loadingInstance = ElLoading.service({
                        text: '删除中...',
                        background: 'rgba(0, 0, 0, .3)'
                    })

                    batchDeleteReq(param).then((res) => {
                        ElMessage.success(res.msg);
                        loadFile();
                    }).finally(() => {
                        loadingInstance.close();
                    });
                }
            }
        });
    }
    // 删除相关 end


    // 文件夹整理 start
    const unFolder = () => {
        ElMessageBox.confirm(`在 <b>${currentPath.value}</b> 下解散文件夹，将移动文件的位置，请确认！`, '提示', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            draggable: true,
            callback: action => {
                if (action === 'confirm') {
                    // 打开全屏 loading
                    const loadingInstance = ElLoading.service({
                        text: '整理中...',
                        background: 'rgba(0, 0, 0, .3)'
                    })
                    let param = {
                        storageKey: storageKey.value,
                        path: currentPath.value,
                    }
                    unFolderReq(param).then(() => {
                        ElMessage.success('整理成功');
                    }).finally(() => {
                        loadingInstance.close();
                        loadFile();
                    });
                }
            }
        });
    }

    const arrangeFolderByTime = (format) => {
        ElMessageBox.confirm(`在 <b>${currentPath.value}</b> 下整理文件夹，将移动文件的位置，请确认！`, '提示', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            draggable: true,
            callback: action => {
                if (action === 'confirm') {
                    // 打开全屏 loading
                    const loadingInstance = ElLoading.service({
                        text: '整理中...',
                        background: 'rgba(0, 0, 0, .3)'
                    })
                    let param = {
                        storageKey: storageKey.value,
                        path: currentPath.value,
                        format: format
                    }
                    arrangeFolderByTimeReq(param).then(() => {
                        ElMessage.success('整理成功');
                    }).finally(() => {
                        loadingInstance.close();
                        loadFile();
                    });
                }
            }
        });
    }
    const arrangeFolderByAddress = (format) => {
        ElMessageBox.confirm(`在 <b>${currentPath.value}</b> 下整理文件夹，将移动文件的位置，请确认！`, '提示', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            draggable: true,
            callback: action => {
                if (action === 'confirm') {
                    // 打开全屏 loading
                    const loadingInstance = ElLoading.service({
                        text: '整理中...',
                        background: 'rgba(0, 0, 0, .3)'
                    })
                    let param = {
                        storageKey: storageKey.value,
                        path: currentPath.value,
                        format: format
                    }
                    arrangeFolderByAddressReq(param).then(() => {
                        ElMessage.success('整理成功');
                    }).finally(() => {
                        loadingInstance.close();
                        loadFile();
                    });
                }
            }
        });
    }
    const arrangeFolderByFileType = () => {
        ElMessageBox.confirm(`在 <b>${currentPath.value}</b> 下整理文件夹，将移动文件的位置，请确认！`, '提示', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            draggable: true,
            callback: action => {
                if (action === 'confirm') {
                    // 打开全屏 loading
                    const loadingInstance = ElLoading.service({
                        text: '整理中...',
                        background: 'rgba(0, 0, 0, .3)'
                    })
                    let param = {
                        storageKey: storageKey.value,
                        path: currentPath.value,
                    }
                    arrangeFolderByFileTypeReq(param).then(() => {
                        ElMessage.success('整理成功');
                    }).finally(() => {
                        loadingInstance.close();
                        loadFile();
                    });
                }
            }
        });
    }
    // 文件夹整理 end

    return {
        batchDownloadFile, rename, newFolder, moveTo, copyTo,
        batchDelete, unFolder, arrangeFolderByTime, arrangeFolderByAddress, arrangeFolderByFileType
    }
}