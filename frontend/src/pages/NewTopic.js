import React, { Component } from 'react';
import { Form, Button } from "react-bootstrap";

import axios from 'axios';

import AuthService from "../services/auth/AuthService";

import authHeader from "../services/auth/auth-header";
import Loading from "../blocks/Loading";

class NewTopic extends Component {

    constructor(props) {
        super(props);
        this.state = {
            currentUser: AuthService.getCurrentUser(),
            name: '',
            tags: '',
            description: '',
            success: null,
            error: null
        };

        this.createTopic = this.createTopic.bind(this);
    }

    handleChange = e => {
        this.setState({ [e.target.name]: e.target.value });
      };

    componentDidMount() {
        if(!this.state.currentUser) {
            this.props.history.push("/signin");
        }
    }

    createTopic = () => {
        this.setState({isLoading: true});
        const data = {
            name: this.state.name,
            title: '1',
            description: this.state.description,
            tags: this.state.tags,
            author: this.state.currentUser.username
        };
        axios.post('/api/topics/', data, { headers: authHeader() })
        .then(res => {
            this.setState({success: true, isLoading:false})
        })
        .catch(err => {
            this.setState({error: true, isLoading:false});
            console.log(err);
        });
    };

    render() {
        const {success, error, isLoading} = this.state;
        if(isLoading)
            return <Loading/>;

        return (
            <div>
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
                <Form onSubmit={this.createTopic}>
                    <Form.Group controlId="formBasicName">
                        <Form.Label>Topic name</Form.Label>
                        <Form.Control name="name" type="text" placeholder="Enter topic name" onChange={this.handleChange} />
                    </Form.Group>
                    <Form.Group controlId="formBasicDescription">
                        <Form.Label>Description</Form.Label>
                        <Form.Control name="description" as="textarea" rows="3" onChange={this.handleChange}/>
                    </Form.Group>
                    <Form.Group controlId="formBasicTags">
                        <Form.Label>Tags</Form.Label>
                        <Form.Control name="tags" type="text" placeholder="Enter tags" onChange={this.handleChange} />
                    </Form.Group>
                    <Button type="submit" variant="primary">Create topic</Button>
                </Form>
            </div>
        );
    }

}

export default NewTopic;