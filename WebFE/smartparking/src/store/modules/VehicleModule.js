import axios from 'axios'
import moment from 'moment'
import BaseModel from './BaseModel';
const actions={
    async getAllVehicleService({commit}){
        const response= await axios.get(BaseModel.baseUrlVehicle)
        debugger
        return response.data;
    },
    async update({commit}, vehicle){
        const response =await axios.put(BaseModel.baseUrlVehicle, vehicle, BaseModel.configJsonHeader);
        return response.data;
    }
}
export default {
    actions,
};