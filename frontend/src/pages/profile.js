/* eslint-disable */
import React, { Component } from 'react';

import AuthService from "../services/auth/AuthService";
import axios from 'axios';
import Loading from "../blocks/Loading";

import authHeader from "../services/auth/auth-header";

class Profile extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            isLoading: false,

            username: null,
            email: null,
            password: null,

            success: false,
            error: false
        };

        this.state.username = this.state.currentUser.username;
        this.state.email = this.state.currentUser.email;

        this.updateUser = this.updateUser.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount() {
        if(!this.state.currentUser) {
            this.props.history.push("/signin");
        }
    }

    updateUser = e => {
        e.preventDefault();
        this.setState({ isLoading: true });

        const {username, email, password, currentUser} = this.state;

        const data = {
            username: username,
            email: email,
            password: password === "" ? null : password
        };

        axios.put('/api/users/'+currentUser.id, data, { headers: authHeader() })
            .then(res => {
                console.log(res.status);
                if(res.status === 200) {
                    currentUser.email = email;
                    localStorage.removeItem("user");
                    localStorage.setItem("user", JSON.stringify(currentUser));
                    this.setState({ isLoading: false, success: true, currentUser: currentUser });
                } else {
                    throw new Error();
                }
            })
            .catch(err => {
                this.setState({isLoading: false, success: false, error: true});
            })
    };

    handleChange = e => {
        this.state[e.target.name] = e.target.value;
        let email = this.state;
        this.state.error = (email === "");
        this.state.success = false;
        this.setState(this.state);
    };

    render() {
        const {currentUser, isLoading, success, error} = this.state;
        if(isLoading) {
            return <Loading/>;
        }
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-12">
                        <div className="card">
                            <div className="card-body">
                                <div className="row">
                                    <div className="col-md-12">
                                        <h4>Your Profile</h4>
                                        <hr/>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-md-12">
                                        <form onSubmit={this.updateUser}>
                                            {success &&
                                                <div className="alert alert-success" role="alert">
                                                    Success!
                                                </div>
                                            }
                                            {error &&
                                                <div className="alert alert-danger" role="alert">
                                                    Error!
                                                </div>
                                            }

                                            <div className="form-group row">
                                                <label htmlFor="email" className="col-4 col-form-label">Email</label>
                                                <div className="col-8">
                                                    <input
                                                        name="email"
                                                        placeholder="Email"
                                                        className="form-control here"
                                                        defaultValue={currentUser.email}
                                                        onChange={this.handleChange}
                                                        type="email"
                                                    />
                                                </div>
                                            </div>

                                            <div className="form-group row">
                                                <label htmlFor="newpass" className="col-4 col-form-label">New Password</label>
                                                <div className="col-8">
                                                    <input
                                                        name="password"
                                                        placeholder="**********"
                                                        className="form-control here"
                                                        type="password"
                                                        onChange={this.handleChange}
                                                    />
                                                </div>
                                            </div>

                                            <div className="form-group row">
                                                <div className="offset-4 col-8">
                                                    <button name="submit" disabled={error} type="submit" className="btn btn-primary">
                                                        Save
                                                    </button>
                                                </div>
                                            </div>

                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Profile;