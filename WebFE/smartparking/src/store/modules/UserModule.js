import axios from 'axios'
import moment from 'moment'
import BaseModel from './BaseModel';
const state = {
};
const getters = {
};
const actions = {
    async getAllUserService({commit}){
        const response= await axios.get(BaseModel.baseUrlUser, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);
    },
    async addUserService({commit}, user){
        const reposnse =await axios.post(BaseModel.baseUrlUser, user, BaseModel.configFormDataHeader())
        return BaseModel.handleResponse(reposnse);
        
    },
    async updateUserService({commit}, user){
        debugger
        const response= await axios.put(BaseModel.baseUrlUser+'/'+JSON.parse(user.get('user')).id, user, BaseModel.configFormDataHeader());
        return BaseModel.handleResponse(response);
    },
    async deleteUserService({commit}, id){
        const response= await axios.delete(BaseModel.baseUrlUser+'/'+id, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);
    },
    async searchUserService({commit}, user){
        const respone =await axios.post(BaseModel.baseUrlUser+'/search', user, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(respone);
    }
    
}
const mutations ={
    
}
export default {
    state,
    getters,
    actions,
    mutations
};