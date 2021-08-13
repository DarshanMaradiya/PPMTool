import { combineReducers } from "redux";
import backlogReducer from "./backlogReducer";
import errorReducer from "./errorReducer";
import projectReducer from "./projectReducer";
import securityReducer from "./securityReducer";

const rootReducer = combineReducers({
    errors: errorReducer,
    project: projectReducer,
    backlog: backlogReducer,
    security: securityReducer
})

export default rootReducer