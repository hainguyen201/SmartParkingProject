import Vuex from 'Vuex';
import Vue from 'vue';
import UserModule from './modules/UserModule';
import ModelModule from './modules/ModelModule';
import EntranceModule from './modules/Entrance';
import DeviceModule from './modules/DeviceModule'
import VehicleModule from './modules/VehicleModule'

// Load Vuex
Vue.use(Vuex);

// Create store
export default new Vuex.Store({
    modules: {
        UserModule,
        ModelModule,
        EntranceModule,
        DeviceModule,
        VehicleModule
    }
});