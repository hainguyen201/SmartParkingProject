import axios from 'axios'
import moment from 'moment'
import BaseModel from './BaseModel';
const state = {
};
const getters = {
};
const actions = {
    async getAllUser({commit}){
        const response= await axios.get(BaseModel.baseUrlUser);
        return response.data;
    },
    async addUserService({commit}, user){
        const reposnse =await axios.post(BaseModel.baseUrlUser, user, BaseModel.configFormDataHeader)
        return reposnse.data;
        
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