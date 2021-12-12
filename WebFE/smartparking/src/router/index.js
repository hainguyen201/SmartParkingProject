import Vue from 'vue'
import Router from 'vue-router'
import User from '@/components/User'
import Parking from '@/components/Parking'
import Device from '@/components/Device'
import Vehicle from '@/components/Vehicle'
import Model from '@/components/Model'
import Login from '@/components/Login'
import Manager from '@/components/Manager'
import Register from '@/components/Register'
import Entrance from '@/components/Entrance'

Vue.use(Router)
export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/manager'
    },
    {
      path:'/manager',
      component:Manager,
      children:[
        {
          path:'/users',
          component:User,
        },
        {
          path:'/parkings',
          component:Parking
        },
        {
          path:'/devices',
          component:Device
        },
        {
          path:'/vehicles',
          component:Vehicle
        }
        ,
        {
          path:'/models',
          component:Model
        },
        {
          path:'/entrances',
          component: Entrance
        }
      ]
    },
    
    {
      path:'/login',
      component:Login
    },
    {
      path:'/sign-up',
      component:Register
    }
  ]
})
