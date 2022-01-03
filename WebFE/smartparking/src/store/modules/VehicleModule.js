import axios from 'axios'
import moment from 'moment'
import BaseModel from './BaseModel';
const actions={
    async getAllVehicleService({commit}){
        const response= await axios.get(BaseModel.baseUrlVehicle, BaseModel.configJsonHeader())
        return BaseModel.handleResponse(response);
    },
    async editVehicleService({commit}, vehicle){
        const response =await axios.put(BaseModel.baseUrlVehicle+'/'+vehicle.get('id'), vehicle, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);

    },
    async addVehicleService({commit}, vehicle){
        const response=await axios.post(BaseModel.baseUrlVehicle, vehicle, BaseModel.configFormDataHeader());
        return BaseModel.handleResponse(response);
    },
    async searchVehicleService({commit}, vehicle){
        const reponse= await axios.post(BaseModel.baseUrlVehicle+"/search", vehicle, BaseModel.configFormDataHeader());
        return BaseModel.handleResponse(reponse);

    },
    async deleteVehicleService({commit}, id){
        const response = await axios.delete(BaseModel.baseUrlVehicle+'/'+id, BaseModel.configJsonHeader())
        return BaseModel.handleResponse(response);
    }
}
export default {
    actions,
};