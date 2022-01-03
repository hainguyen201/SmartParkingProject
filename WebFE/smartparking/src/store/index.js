import Vuex from 'Vuex';
import Vue from 'vue';
import UserModule from './modules/UserModule';
import ModelModule from './modules/ModelModule';
import EntranceModule from './modules/Entrance';
import DeviceModule from './modules/DeviceModule'
import VehicleModule from './modules/VehicleModule'
import AuthModule from './modules/AuthModule'
import ParkingAreaModule from './modules/ParkingAreaModule'
import UtilsModule from './modules/UtilsModule'
import ParkingSlotModule from './modules/ParkingSlotModule'

// Load Vuex
Vue.use(Vuex);

// Create store
export default new Vuex.Store({
    modules: {
        UtilsModule,
        UserModule,
        ModelModule,
        EntranceModule,
        DeviceModule,
        VehicleModule,
        AuthModule,
        ParkingAreaModule,
        ParkingSlotModule
    }
});