import axios from 'axios'
import BaseModel from './BaseModel';
const actions={
    async getAllDeviceService({commit}){
        const response= await axios.get(BaseModel.baseUrlDevice)
        return BaseModel.handleResponse(response);
    },
    async updateDeviceService({commit}, device){
        const response =await axios.put(BaseModel.baseUrlDevice+'/'+device.id, device, BaseModel.configJsonHeader);
        return BaseModel.handleResponse(response);
        
    },
    async addDeviceService({commit}, device){
        const response =await axios.post(BaseModel.baseUrlDevice, device, BaseModel.configJsonHeader);
        return BaseModel.handleResponse(response);
    },
    async deleteDeviceService({commit}, id){
        const response= await axios.delete(BaseModel.baseUrlDevice+'/'+id,BaseModel.configJsonHeader);
        return BaseModel.handleResponse(response);
    },
    async searchDeviesService({commit}, device){
        const response= await axios.post(BaseModel.baseUrlDevice+'/search', device, BaseModel.configJsonHeader);
        return BaseModel.handleResponse(response);
    }
}
export default {
    actions,
};