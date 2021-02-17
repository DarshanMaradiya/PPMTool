import axios from "axios"
import { GET_ERRORS } from "./types"

export const addProjectTask = (backlog_id, project_task, history) => async dispatch => {
    try{
        await axios.post(`/api/backlog/${backlog_id}`, project_task)
        history.push(`/projectBoard/${backlog_id}`)
        // Clearing all errors
        dispatch({
            type: GET_ERRORS,
            payload: {} // by passing empty object
        })
    } catch (err) {
        dispatch({
            type: GET_ERRORS,
            payload: err.response.data
        })
    }
}