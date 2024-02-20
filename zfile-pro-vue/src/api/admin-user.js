import axios from "~/http/request"


// 获取所有用户
export const loadUserListReq = (data) => {
    return axios({
        url: "/admin/user/list",
        method: "get",
        data
    })
}

// 获取指定用户的所有参数
export const loadUserItemReq = (id) => {
    return axios({
        url: `/admin/user/${id}/get`,
        method: "get"
    })
}

// 是否存在此用户
export const existUsernameReq = (data) => {
    return axios({
        url: `/admin/user/exist`,
        method: "get",
        data
    })
}
// 删除用户
export const deleteUserReq = (id) => {
    return axios({
        url: `/admin/user/${id}/del`,
        method: "delete",
    })
}
// 重置密码
export const resetPasswordReq = (id, data) => {
    return axios({
        url: `/admin/user/${id}/resetPassword`,
        method: "post",
        data
    })
}
// 启用/关闭用户
export const userSwitchEnableReq = (id, action) => {
    return axios({
        url: `/admin/user/${id}/${action}`,
        method: "post",
    })
}

// 保存用户
export const userSaveReq = (data) => {
    return axios({
        url: `/admin/user/save`,
        method: "post",
        data
    })
}

