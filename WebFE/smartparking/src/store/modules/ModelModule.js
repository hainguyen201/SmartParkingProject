import axios from 'axios'
import moment from 'moment'
import BaseModel from './BaseModel';
const state = {
    license: ''
};
const getters = {
    licenseImage: state=> state.license
    
};
const actions = {
    async getLicense({ commit }, fd) {
        const response = await axios.post(BaseModel.baseUrlModel+"/test", fd, BaseModel.configFormDataHeader());
        return BaseModel.handleResponse(response); 
    },
    async getAllModel({commit}){
        console.log(BaseModel.configJsonHeader());
        const response= await axios.get(BaseModel.baseUrlModel, BaseModel.configJsonHeader());
        return response.data;
    },
    async addModel({commit}, model){
        const response= await axios.post(BaseModel.baseUrlModel, model, BaseModel.configJsonHeader())
        return response.data;
    },
    async getModelById({commit}, id){
        const response= await axios.get(BaseModel.baseUrlModel+"/"+id, BaseModel.configJsonHeader())
        return response.data;
    },
    async deleteModel({commit}, id){
        const response= await axios.delete(BaseModel.baseUrlModel+"/"+id,BaseModel.configJsonHeader())
        return response.data;
    },
    async updateModelService({commit}, model){
        const response= await axios.put(BaseModel.baseUrlModel+'/'+model.id,model, BaseModel.configJsonHeader())
        return response.data;
    },
    async useModelStatusService({commit},  id){
        console.log(BaseModel.configJsonHeader())
        const response= await axios.put(BaseModel.baseUrlModel+'/status/'+id,'', BaseModel.configJsonHeader())
        return response.data;
    }
}
const mutations ={
    setLicense: (state, license)=>(state.license=license)
}
export default {
    state,
    getters,
    actions,
    mutations
};