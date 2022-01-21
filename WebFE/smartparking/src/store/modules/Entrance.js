import axios from 'axios'
import moment from 'moment'
import BaseModel from './BaseModel';
const actions={
    async getAllGateService({commit}){
        const response= await axios.get(BaseModel.baseUrlGate, BaseModel.configJsonHeader())
        return BaseModel.handleResponse(response)
    },
    async updateGateService({commit}, gate){
        const respone= await axios.put(BaseModel.baseUrlGate+"/"+gate.gateId,JSON.stringify(gate), BaseModel.configJsonHeader())
        return BaseModel.handleResponse(respone)
    },
    async addGateService({commit}, gate){
        const response= await axios.post(BaseModel.baseUrlGate, JSON.stringify(gate), BaseModel.configJsonHeader())
        return BaseModel.handleResponse(response)
    },
    async deleteGateService({commit}, id){
        const response= await axios.delete(BaseModel.baseUrlGate+'/'+id, BaseModel.configJsonHeader())
        return BaseModel.handleResponse(response)
    },
    async controlGateService({commit}, command){
       var gateId=command.gateId;
       var commandType=command.commandType;
       const response = await axios.get(BaseModel.baseUrlGate+'/'+gateId+'/'+commandType, BaseModel.configJsonHeader())
       return BaseModel.handleResponse(response);

    }
}
export default{
    actions
}