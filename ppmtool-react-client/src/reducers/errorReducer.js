import { CLEAR_ERRORS, GET_ERRORS } from "../actions/types"

const initialState = {}

const errorReducer = (state = initialState, action) => {
    switch(action.type) {
        case GET_ERRORS:
            return action.payload
        case CLEAR_ERRORS:
            return action.payload
        default: return state
    }
}

export default errorReducer