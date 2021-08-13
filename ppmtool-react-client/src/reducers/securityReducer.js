import { CLEAR_CURRENT_USER, SET_CURRENT_USER } from "../actions/types"

const intialState = {
    user: {},
    validToken: false
}

// whether we have user in payload or not
const booleanActionPayload = payload => {
    if (payload) return true
    return false
}

const securityReducer = (state = intialState, action) => {
    switch (action.type) {
        case SET_CURRENT_USER:
            return {
                ...state,
                validToken: booleanActionPayload(action.payload),
                user: action.payload
            }
        case CLEAR_CURRENT_USER:
            return {
                ...state,
                validToken: false,
                user: {}
            }
        default:
            return state
    }
}

export default securityReducer