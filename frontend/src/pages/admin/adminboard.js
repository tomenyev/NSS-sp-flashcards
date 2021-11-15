/* eslint-disable */
import React, { Component } from 'react';

import AuthService from "../../services/auth/AuthService";
import axios from 'axios';
import Loading from "../../blocks/Loading";

import authHeader from "../../services/auth/auth-header";
import {Button, Card, CardColumns, Form, Nav, ListGroup, CardGroup} from "react-bootstrap";
import CustomModal from "../../blocks/CustomModal";

import AdminTopics from "./admintopics";
import AdminReviews from "./adminreviews";
import AdminUsers from "./adminusers";

class AdminBoard extends Component {

    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            isLoading: false,
        };

        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount() {
    }

    handleChange = (e)=> {
        e.preventDefault();
        this.state[e.target.name] = e.target.value;
    };


    createUser = (e) => {
        e.preventDefault();
        this.setState({isLoading: true});
        const data = {
            username: this.state.createUsername,
            email: this.state.createEmail,
            password: this.state.createPassword
        };
        axios.post('/api/users/?roleName=ROLE_'+this.state.createRole, data, { headers: authHeader() })
            .then(res => {
                this.state.users.push(res.data);
                this.setState({isLoading: false, users: this.state.users});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            });
    };

    render() {
        const {currentUser, isLoading} = this.state;
        if(isLoading) {
            return <Loading/>;
        }

        const hash = this.props.location.hash;
        return (
            <div className="container">
                <Card>
                    <Card.Header>
                        <Nav variant="pills" defaultActiveKey="#first">
                            <Nav.Item>
                                <Nav.Link href="#users" className={hash === "#users" || hash === "" ? "active" : ""}>Users</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link href="#topics" className={hash === "#topics" ? "active" : ""}>Topics</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link href="#reviews" className={hash === "#reviews" ? "active" : ""}>Reviews</Nav.Link>
                            </Nav.Item>
                        </Nav>
                    </Card.Header>
                    <Card.Body>
                        {
                            (hash === "#users" || hash === "") &&
                                <AdminUsers history={this.props.history}/>
                        }
                        {
                            hash === "#topics" &&
                                <AdminTopics history={this.props.history}/>
                        }
                        {
                            hash === "#reviews" &&
                                <AdminReviews history={this.props.history}/>
                        }
                    </Card.Body>
                    <Card.Footer hidden={!(hash === "#users" || hash === "")}>
                        <div className="btn-group">
                            <CustomModal name={"Create"} title={"Create user"} body={
                            <div>
                                <Form onSubmit={this.createUser}>
                                    <Form.Group controlId="formBasicName">
                                        <Form.Label>Username</Form.Label>
                                        <Form.Control name="createUsername" type="text" placeholder="Enter username" onChange={this.handleChange} />
                                    </Form.Group>
                                    <Form.Group controlId="formBasicName">
                                        <Form.Label>Email</Form.Label>
                                        <Form.Control name="createEmail" type="email" placeholder="Enter email" onChange={this.handleChange} />
                                    </Form.Group>
                                    <Form.Group controlId="formBasicName">
                                        <Form.Label>Password</Form.Label>
                                        <Form.Control name="createPassword" type="password" placeholder="Enter password" onChange={this.handleChange} />
                                    </Form.Group>
                                    <Form.Group controlId="formBasicName">
                                        <Form.Label>Role</Form.Label>
                                        <Form.Control as="select" name="createRole" onChange={this.handleChange}>
                                            <option>USER</option>
                                            <option>ADMIN</option>
                                        </Form.Control>
                                    </Form.Group>
                                    <div className='text-center'>
                                        <Button type="submit" variant="primary">Create user</Button>
                                    </div>
                                </Form>
                            </div>
                            }/>
                        </div>
                    </Card.Footer>
                </Card>
            </div>
        );
    }
}

export default AdminBoard;