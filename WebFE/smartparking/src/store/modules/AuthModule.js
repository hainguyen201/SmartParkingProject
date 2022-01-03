import axios from 'axios'
import BaseModel from './BaseModel';
const actions = {
    async loginService({commit}, user){
        try{
            const configJsonHeader={
                headers: {
                    'Content-Type': 'application/json'
                }
            }
            const response=await axios.post(BaseModel.baseUrlAuth, user, configJsonHeader)
            return response;
        }catch(error){
            console.error(error);
            return 'error';
        }
        
    }
}

export default {
    actions
};