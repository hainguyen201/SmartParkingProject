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
    async getLicense({ commit }, modelUrl, fd) {
        console.log("after:", fd)
        if(modelUrl === null || modelUrl === ''){
            
            const response = await axios.post("http://localhost:8787/license", fd, BaseModel.configFormDataHeader);
            console.error("model url is null, run temprory");
            return response.data;
        }else{
            const response = await axios.post(modelUrl, fd, BaseModel.configFormDataHeader);
            
            return response.data;
        }
        
        
    },
    async getAllModel({commit}){
        const response= await axios.get(BaseModel.baseUrlModel);
        return response.data;
    },
    async addModel({commit}, model){
        const response= await axios.post(BaseModel.baseUrlModel, model, BaseModel.configJsonHeader)
        return response.data;
    },
    async getModelById({commit}, id){
        const response= await axios.get(BaseModel.baseUrlModel+"/"+id)
        return response.data;
    },
    async deleteModel({commit}, id){
        const response= await axios.delete(BaseModel.baseUrlModel+"/"+id)
        return response.data;
    },
    async updateModelService({commit}, model){
        const response= await axios.put(BaseModel.baseUrlModel+'/'+model.id,model, BaseModel.configJsonHeader)
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