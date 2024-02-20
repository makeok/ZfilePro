import axios from "~/http/request"


// 获取指定用户的所有参数
export const loadUserItemReq = (id) => {
    return axios({
        url: `/api/user/${id}/get`,
        method: "get"
    })
}

// 保存用户
export const userSaveReq = (data) => {
    return axios({
        url: `/api/user/save`,
        method: "post",
        data
    })
}

// 重置密码
export const resetPasswordReq = (id, data) => {
    return axios({
        url: `/api/user/${id}/resetPassword`,
        method: "post",
        data
    })
}


