import React, { Component } from 'react'
import { connect } from 'react-redux'
import { createProject } from '../../actions/projectActions'
import PropTypes from "prop-types"
import store from '../../store'
import { deepCompare } from '../../UDFs'

class AddProject extends Component {
    constructor() {
        super()
    
        this.state = {
            projectName: "",
            projectIdentifier: "",
            description: "",
            start_date: "",
            end_date: "",
            errors: {
                projectName: "",
                projectIdentifier: "",
                description: "",
            }
        }
    }

    onChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    // Lifecycle hooks
    // Following is deprecated
    // UNSAFE_componentWillReceiveProps(nextProps) {
    //     if(nextProps.errors) {
    //         this.setState({
    //             errors: nextProps.errors
    //         })
    //     }
    // }
    // 
    // Following is alternative
    static getDerivedStateFromProps(nextProps, prevState) {
        if (!deepCompare(prevState.errors, nextProps.errors)) {
          return ({ errors: nextProps.errors }) // <- this is setState equivalent
        }
        return null
      }

    onSubmit = (e) => {
        e.preventDefault()
        const newProject = {
            projectName: this.state.projectName,
            projectIdentifier: this.state.projectIdentifier,
            description: this.state.description,
            start_date: this.state.start_date,
            end_date: this.state.end_date
        }
        this.props.createProject(newProject, this.props.history)
    }
    
    render() {
        {
            //check name attribute input fields
            //create constructor
            //set state
            //set value on input fields
            //create onChange function
            //set onChange on each input field
            //bind on constructor
            //check state change in the react extension
        }
        const { errors } = this.state
        return (
            <div className="register">
                <div className="container">
                    <div className="row">
                        <div className="col-md-8 m-auto">
                            <h5 className="display-4 text-center">Create Project form</h5>
                            <hr />
                            <form onSubmit={ this.onSubmit }>
                                <div className="form-group">
                                    <input 
                                        type="text" 
                                        className="form-control form-control-lg " 
                                        placeholder="Project Name" 
                                        name="projectName"
                                        value={ this.state.projectName }
                                        onChange={ this.onChange }
                                    />
                                    <p>{ errors.projectName }</p>
                                </div>
                                <div className="form-group">
                                    <input 
                                        type="text" 
                                        className="form-control form-control-lg" 
                                        placeholder="Unique Project ID" 
                                        name="projectIdentifier"
                                        value={ this.state.projectIdentifier }
                                        onChange={ this.onChange }
                                    />
                                    <p>{ errors.projectIdentifier }</p>
                                </div>
                                
                                <div className="form-group">
                                    <textarea 
                                        className="form-control form-control-lg" 
                                        placeholder="Project Description"
                                        name="description"
                                        value={ this.state.description }
                                        onChange={ this.onChange }
                                    ></textarea>
                                    <p>{ errors.description }</p>
                                </div>
                                <h6>Start Date</h6>
                                <div className="form-group">
                                    <input 
                                        type="date" 
                                        className="form-control form-control-lg" 
                                        name="start_date" 
                                        value={ this.state.start_date }
                                        onChange={ this.onChange }
                                    />
                                    <p>{ errors.start_date }</p>
                                </div>
                                <h6>Estimated End Date</h6>
                                <div className="form-group">
                                    <input 
                                        type="date" 
                                        className="form-control form-control-lg" 
                                        name="end_date" 
                                        value={ this.state.end_date }
                                        onChange={ this.onChange }
                                    />
                                    <p>{ errors.end_date }</p>
                                </div>

                                <input 
                                    type="submit" 
                                    className="btn btn-primary btn-block mt-4" 
                                />
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

// this is like a constraint, we are telling react that createProject func is required of given proptype
// to work properly
AddProject.propTypes = {
    createProject: PropTypes.func.isRequired,
    errors: PropTypes.object
}

const mapStateToProps = state => ({
    errors: {
        ...state.errors
    }
})

// createProject action is passed
export default connect(mapStateToProps, { createProject })(AddProject)