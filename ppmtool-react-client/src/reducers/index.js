import { combineReducers } from "redux";
import backlogReducer from "./backlogReducer";
import errorReducer from "./errorReducer";
import projectReducer from "./projectReducer";

const rootReducer = combineReducers({
    errors: errorReducer,
    project: projectReducer,
    backlog: backlogReducer
})

export default rootReducer