import { createStore } from 'vuex'
import request from '@/utils/request'

export default createStore({
  state: {
    user: null,
    token: localStorage.getItem('token') || '',
    userId: localStorage.getItem('userId') || '',
    username: localStorage.getItem('username') || '',
    nickname: localStorage.getItem('nickname') || ''
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
    },
    SET_TOKEN(state, { token, userId, username, nickname }) {
      state.token = token
      state.userId = userId
      state.username = username
      state.nickname = nickname || username
      localStorage.setItem('token', token)
      localStorage.setItem('userId', userId)
      localStorage.setItem('username', username)
      localStorage.setItem('nickname', nickname || username)
    },
    SET_NICKNAME(state, nickname) {
      state.nickname = nickname
      localStorage.setItem('nickname', nickname)
    },
    CLEAR_USER(state) {
      state.user = null
      state.token = ''
      state.userId = ''
      state.username = ''
      state.nickname = ''
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      localStorage.removeItem('nickname')
    }
  },
  actions: {
    async getUserInfo({ commit }) {
      try {
        const res = await request.get('/auth/info')
        commit('SET_USER', res.data)
        return res.data
      } catch (error) {
        throw error
      }
    },
    async login({ commit }, loginData) {
      try {
        const res = await request.post('/auth/login', loginData)
        commit('SET_TOKEN', res.data)
        return res
      } catch (error) {
        throw error
      }
    },
    logout({ commit }) {
      commit('CLEAR_USER')
    }
  }
})
