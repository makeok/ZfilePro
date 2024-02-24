import { defineStore } from 'pinia'

// @ts-ignore
import common from "~/common";
import useStorageConfigStore from "./storage-config";

// 当前存储源的配置信息，数据来源为服务端配置。请求存储源后会获取其配置信息。
const useFileDataStore = defineStore('fileDataStore', {
  state: () => {
    return {
      currentPreviewRow: {},
      currentPreviewMode: 'list',
      currentClickRow: {},
      currentRightClickRow: {},
      currentStorageSource: {
        id: null,
        type: {
          description: '',
          key: ''
        }
      },
      imgMode: false,
      newImgMode: false,
      oldStorageKey: null,
      searchParam: '',
      fileListSource: [],
      loadFileSize: -1,
      audioArray: [],
      audioIndex: 0,
      searchKey: '',
    }
  },
  getters: {
    filterFileByType: (state) => {
      return (type: string) => {
        if(state.currentPreviewMode == 'repeat'){
          let repeatList = [];
          if(state.currentPreviewRow != null){
            repeatList.push(state.currentPreviewRow)
          }
          return repeatList;
        }
        return state.fileListSource.filter(function (item:any) {
          if (item.type === 'BACK') {
            return false
          }
          let name = item.name;
          let suffix = name.substr(name.lastIndexOf('.') + 1).toLowerCase();
          return common.constant.fileTypeMap[type].indexOf(suffix) !== -1;
        });
      };
    },
    fileList: state => {
      if(state.currentPreviewMode == 'repeat'){
        let repeatList = [];
          if(state.currentPreviewRow != null){
            repeatList.push(state.currentPreviewRow)
          }
          return repeatList;
      }
      if (state.loadFileSize === -1) return [];
      let firstIsBack = state.fileListSource[0]?.type === 'BACK';
      let toSize = firstIsBack ? state.loadFileSize + 1 : state.loadFileSize;
      toSize = toSize > state.fileListSource.length ? state.fileListSource.length : toSize;
      let tableData = state.fileListSource.slice(0, toSize);
      tableData.forEach((item:any) => {
        // 生成图标
        if (!item.icon) {
          item['icon'] = common.getFileIconName(item);
        }
        if (item.preview !== null) {
          // 获取文件类型
          let fileType = common.getFileType(item.name);
          if (fileType) {
            // 获取文件是否可预览
            item['fileType'] = fileType;
            item.preview = common.constant.previewFileType.indexOf(fileType) !== -1;
          } else {
            item.preview = false;
          }
        }
      });
      return tableData;
    },
    getFileUrlByName: state => {
      return (name: string) => {
        let item = state.fileListSource.find((item:any) => item.name === name);
        if (item) {
          return item.url;
        }
        return '';
      };
    }
  },
  actions: {
    updateCurrentStorageSource(val: any) {
      this.currentStorageSource = val;
    },
    updateAudioIndex(val: any) {
      this.audioIndex = val;
    },
    updateAudioList(val: any) {
      this.audioArray = val;
    },
    updateCurrentClickRow(val: any) {
      this.currentClickRow = val;
    },
    updateCurrentPreviewRow(val: any) {
      this.currentPreviewRow = val;
    },
    updateCurrentPreviewMode(val: any) {
      this.currentPreviewMode = val;
    },
    updateCurrentRightClickRow(val: any) {
      this.currentRightClickRow = val;
    },
    updateFileList(val: any) {
      this.fileListSource = val;
      this.loadFileSize = useStorageConfigStore().globalConfig.maxShowSize;
    },
    updateOldStorageKey(val: any) {
      this.oldStorageKey = val;
    },
    updateLoadFileSize(val: number) {
      this.loadFileSize = val;
    }
  },
})

export default useFileDataStore;