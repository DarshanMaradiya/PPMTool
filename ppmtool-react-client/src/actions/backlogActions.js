import axios from "axios"
import { CLEAR_ERRORS, DELETE_PROJECT_TASK, GET_BACKLOG, GET_ERRORS, GET_PROJECT_TASK } from "./types"

export const addProjectTask = (backlog_id, project_task, history) => async dispatch => {
    try{
        await axios.post(`/api/backlog/${backlog_id}`, project_task)
        history.push(`/projectBoard/${backlog_id}`)
        // Clearing all errors
        dispatch({
            type: CLEAR_ERRORS,
            payload: {} // by passing empty object
        })
    } catch (err) {
        dispatch({
            type: GET_ERRORS,
            payload: err.response.data
        })
    }
}

export const getBacklog = (backlog_id) => async dispatch => {
    try{
        const res = await axios.get(`/api/backlog/${backlog_id}`)
        dispatch({
            type: GET_BACKLOG,
            payload: res.data
        })
        // Clearing all errors
        dispatch({
            type: CLEAR_ERRORS,
            payload: {} // by passing empty object
        })
    } catch (err) {
        dispatch({
            type: GET_ERRORS,
            payload: err.response.data
        })
    }
}

export const getProjectTask = (backlog_id, projectTask_id, history) => async dispatch => {
    try{
        const res = await axios.get(`/api/backlog/${backlog_id}/${projectTask_id}`)
        dispatch({
            type: GET_PROJECT_TASK,
            payload: res.data
        })
        // Clearing all errors
        dispatch({
            type: CLEAR_ERRORS,
            payload: {} // by passing empty object
        })
    } catch (err) {
        history.push('/dashboard')
        dispatch({
            type: GET_ERRORS,
            payload: err.response.data
        })
    }
}

export const updateProjectTask = (backlog_id, project_task, history) => async dispatch => {
    try{
        await axios.post(`/api/backlog/${backlog_id}`, project_task)
        history.push(`/projectBoard/${backlog_id}`)
        // Clearing all errors
        dispatch({
            type: CLEAR_ERRORS,
            payload: {} // by passing empty object
        })
    } catch (err) {
        dispatch({
            type: GET_ERRORS,
            payload: err.response.data
        })
    }
}

export const deleteProjectTask = (backlog_id, projectTask_id) => async dispatch => {
    if(window.confirm(`You are deleting project task ${projectTask_id}, this action can not be undone!`)) {
        await axios.delete(`/api/backlog/${backlog_id}/${projectTask_id}`)
        dispatch({
            type: DELETE_PROJECT_TASK,
            payload: projectTask_id
        })
    }
}