import {ElLoading} from "element-plus";
import {batchDeleteReq, findRepeatFileReq} from "~/api/file-operator";
import axios from "axios";
import common from "~/common";

import useStorageConfigStore from "~/stores/storage-config";
let storageConfigStore = useStorageConfigStore();
import useFilePwd from "~/composables/file/useFilePwd";
let { getPathPwd } = useFilePwd();

import useRouterData from "~/composables/useRouterData";

// 引入文件预览组件
import useFilePreview from '~/composables/file/useFilePreview';
const { openAudio, openImage, openOffice, openPdf, openText, openVideo, open3d } = useFilePreview();
import useFileOperator from '~/composables/file/useFileOperator';

const repeatFileList = ref([]);
const repeatDeleteList = ref([]);
const repeatVisible = ref(false);

export default function useFileRepeat() {

    let { storageKey, currentPath } = useRouterData();

    // 查找重复文件
    const findRepeatFile = () => {
        // 打开全屏 loading
        const loadingInstance = ElLoading.service({
            text: '检索中，可能会花费一些时间...',
            background: 'rgba(0, 0, 0, .3)'
        })
        let param = {
            storageKey: storageKey.value,
            path: currentPath.value,
        }
        findRepeatFileReq(param).then((res) => {
            repeatFileList.value = res.data;
            ElMessage.success('获取重复文件成功');
        }).finally(() => {
            loadingInstance.close();
        });
    }
    // 删除相关 start
    const batchDelete = () => {
        if (!storageConfigStore.permission.delete) {
            return;
        }

        if (repeatDeleteList.value.length === 0) {
            ElMessage.warning('请先至少选中一个文件或文件夹！');
            return;
        }

        let deleteConfirmMsg = repeatDeleteList.value.length === 1 ? '是否确认删除 ' : '是否确认批量删除 ';
        if (repeatDeleteList.value.length > 0) {
            deleteConfirmMsg += (repeatDeleteList.value.length + ' 个文件');
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
                    
                    repeatDeleteList.value.forEach((item) => {
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
                        findRepeatFile();
                    }).finally(() => {
                        loadingInstance.close();
                    });
                }
            }
        });
    }
    // 删除相关 end

    // 点击文件时，判断是文件夹则进入文件夹，是文件则进行预览
    const openRow = (row) => {
        const { batchDownloadFile } = useFileOperator();
        batchDownloadFile(row);
        // 获取文件类型
        // let fileType = common.getFileType(row.name);
        // switch (fileType) {
        //     case 'video': openVideo(row); break;
        //     case 'image': openImage(row); break;
        //     case 'text': openText(row); break;
        //     case 'audio': openAudio(row); break;
        //     case 'office': openOffice(row); break;
        //     case 'pdf': openPdf(row); break;
        //     case 'three3d': open3d(row); break;
        //     default: batchDownloadFile(row);
        // }
    }

    // 打开复制文件 dialog
    const openRepeatDialog = () => {
        repeatVisible.value = true;
    }

    return {
        openRepeatDialog, repeatVisible, repeatFileList, findRepeatFile,repeatDeleteList,batchDelete,openRow
    }
}