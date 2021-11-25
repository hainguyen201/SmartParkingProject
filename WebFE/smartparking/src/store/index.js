import Vuex from 'Vuex';
import Vue from 'vue';
import UserModule from './modules/UserModule';
import ModelModule from './modules/ModelModule';

// Load Vuex
Vue.use(Vuex);

// Create store
export default new Vuex.Store({
    modules: {
        UserModule,
        ModelModule
    }
});