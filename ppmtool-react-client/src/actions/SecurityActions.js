import axios from "axios"
import jwtDecode from "jwt-decode"
import setJWTToken from "../securityUtils/setJWTToken"
import { CLEAR_ERRORS, GET_ERRORS, SET_CURRENT_USER } from "./types"

export const createNewUser = (newUser, history) => async dispatch => {
    try {
        await axios.post("/api/users/register", newUser)
        history.push("/login")
        dispatch({
            type: CLEAR_ERRORS,
            payload: {}
        })
    } catch (error) {
        dispatch({
            type: GET_ERRORS,
            payload: error.response.data
        })
    }
}

export const login = LoginRequest => async dispatch => {
    
    try {
        // post the endpoint
        const res = await axios.post("/api/users/login", LoginRequest)
        // extract the token from res data
        const { token } = res.data
        // store the token in local storage
        localStorage.setItem("jwtToken", token)
        // set out token headers onwards
        setJWTToken(token)
        // decode the token on React
        const decoded = jwtDecode(token)
        // clear the errors
        dispatch({
            type: CLEAR_ERRORS,
            payload: {}
        })
        // dispatch to our securityReducer
        dispatch({
            type: SET_CURRENT_USER,
            payload: decoded
        })
        
    } catch (error) {
        dispatch({
            type: GET_ERRORS,
            payload: error.response.data
        })
    }
    
}

export const logout = () => dispatch => {
    localStorage.removeItem("jwtToken")
    setJWTToken(false)
    dispatch({
        type: SET_CURRENT_USER
    })
    dispatch({
        type: CLEAR_ERRORS,
        payload: {}
    })
}