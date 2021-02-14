import { GET_PROJECTS } from "../actions/types"

const initialState = {
    projects: [],
    project: {}
}

const projectReducer = (state = initialState, action) => {
    switch(action.type) {
        case GET_PROJECTS:
            return {
                ...state,
                projects: action.payload 
            }
        default:
            return state
    }
}

export default projectReducer