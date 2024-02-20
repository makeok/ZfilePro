import { defineStore } from "pinia";

import useFileSelect from "~/composables/file/useFileSelect";

let { selectStatistics } = useFileSelect();


// 当前存储源的配置信息，数据来源为服务端配置。请求存储源后会获取其配置信息。
const useStorageConfigStore = defineStore('storageConfigStore', {
  state: () => {
    return {
      globalConfig: {
        siteName: '',
        directLinkPrefix: '',
        infoEnable: false,
        showLinkBtn: false,
        recordDownloadLog: false,
        showShortLink: false,
        showPathLink: false,
        tableSize: 'small',
        rootShowStorage: true,
        fileClickMode: 'dbclick',
        showDocument: false,
        debugMode: false,
        domain: '',
        icp: '',
        avatar: '',
        announcement: '',
        layout: 'full',
        showAnnouncement: false,
        searchEnable: false,
        showLogin: false,
        siteHomeName: '首页',
        siteHomeLogo: '',
        siteHomeLogoLink: '',
        siteHomeLogoTargetMode: '',
        maxShowSize: 1000,
        loadMoreSize: 50,
        defaultSortField: 'name',
        defaultSortOrder: 'asc',
        linkExpireTimes: ''
      },
      folderConfig: {
        readmeText: null,
        readmeDisplayMode: null,
        defaultSwitchToImgMode: false,
        enableFileOperator: false,
      }
    }
  },
  getters: {
    permission: (state) => {
      return {
        open: selectStatistics.value.isSingleSelect && selectStatistics.value.isAllFolder,
        preview: selectStatistics.value.isAllFile && selectStatistics.value.isSingleSelect,
        download: selectStatistics.value.isAllFile,
        link: selectStatistics.value.isAllFile && state.globalConfig.showLinkBtn && (state.globalConfig.showShortLink || state.globalConfig.showPathLink),
        rename: state.folderConfig.enableFileOperator && selectStatistics.value.isSingleSelect,
        delete: state.folderConfig.enableFileOperator,
        newFolder: state.folderConfig.enableFileOperator && selectStatistics.value.isAllFile,
        copy: state.folderConfig.enableFileOperator && selectStatistics.value.isAllFile && !selectStatistics.value.isNoneSelect,
        copyFolder: state.folderConfig.enableFileOperator && selectStatistics.value.isAllFolder && !selectStatistics.value.isNoneSelect,
        move: state.folderConfig.enableFileOperator && selectStatistics.value.isAllFile && !selectStatistics.value.isNoneSelect,
        moveFolder: state.folderConfig.enableFileOperator && selectStatistics.value.isAllFolder && !selectStatistics.value.isNoneSelect,
        upload: state.folderConfig.enableFileOperator && selectStatistics.value.isAllFile,
        arrangeFolder: state.folderConfig.enableFileOperator && (selectStatistics.value.isNoneSelect || selectStatistics.value.isAllFile),
        pathLink: state.globalConfig.showPathLink,
        shortLink: state.globalConfig.showShortLink,
        packageDownload: false,
        batchDownload: true,
      }
    }
  },
  actions: {
    updateGlobalConfig(val: any) {
      this.globalConfig = val;
    },
    updateFolderConfig(val: any) {
      this.folderConfig = val;
    }
  },
})

export default useStorageConfigStore;
