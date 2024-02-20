import {ElLoading} from "element-plus";
import {uploadFileReq} from "~/api/file-operator";
import axios from "axios";
import common from "~/common";

import useRouterData from "~/composables/useRouterData";

const moveVisible = ref(false);
// 当前移动模式, 'file' or 'folder'
const moveMode = ref('');

const copyMoveVisible = ref(false);
// 当前复制模式, 'file' or 'folder'
const isFile = ref(false);
// 当前复制模式, 'copy' or 'move'
const copyMoveMode = ref('copy');

export default function useFileCopyMove() {

    let { storageKey, currentPath } = useRouterData();


    // 打开复制文件 dialog
    const openCopyDialog = () => {
        copyMoveVisible.value = true;
        isFile.value = true;
        copyMoveMode.value = 'copy';
        // visible.value = true;
        // uploadMode.value = 'file';
    }

    // 打开复制文件夹 dialog
    const openCopyFolderDialog = () => {
        copyMoveVisible.value = true;
        isFile.value = false;
        copyMoveMode.value = 'copy';
    }

    // 打开复制文件 dialog
    const openMoveDialog = () => {
        copyMoveVisible.value = true;
        isFile.value = true;
        copyMoveMode.value = 'move';
    }

    // 打开复制文件夹 dialog
    const openMoveFolderDialog = () => {
        copyMoveVisible.value = true;
        isFile.value = false;
        copyMoveMode.value = 'move';
    }

    return {
        copyMoveVisible, copyMoveMode, isFile, openCopyDialog, openCopyFolderDialog,
        openMoveDialog, openMoveFolderDialog,
    }
}