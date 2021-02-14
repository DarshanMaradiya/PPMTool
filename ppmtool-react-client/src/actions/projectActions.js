const { default: axios } = require("axios")
const { GET_ERRORS, GET_PROJECTS } = require("./types")

// It is taking project object and history
// history allows us to redirect once we submit the form
// since we use route to render the UI comp
// Async dispatch is passed cause we want to dispatch
// Async will return promise and we use await to wait for its result
export const createProject = (project, history) => async dispatch => {
    // wrapping in try catch block
    try{
        // use of await
        const response = await axios.post
        // route for posting the valid project object
        ('http://localhost:8080/api/project', project)
        // redirecting to the dashboard
        history.push("/dashboard")
    } catch (err) {
        // If error, dispatch error action object
        dispatch({
            type: GET_ERRORS,
            payload: err.response.data
        })
    }
}

export const getProjects = () => async dispatch => {
    const res = await axios.get('http://localhost:8080/api/project/all')
    dispatch({
        type: GET_PROJECTS,
        payload: res.data
    })
}