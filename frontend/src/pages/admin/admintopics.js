import React, { Component } from 'react';

import { Button, ButtonGroup, Form, Row, Col } from 'react-bootstrap';
import AuthService from "../../services/auth/AuthService";
import axios from 'axios';
import BootstrapTable from 'react-bootstrap-table-next';
import Loading from "../../blocks/Loading";

import authHeader from "../../services/auth/auth-header";

class AdminTopics extends Component {

    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            isLoading: false,
            disabled: true,
            search: null,
            btn: null,
            id: null,
            topics: [],

            name: null,
            title: null,
            description: null,
            tags: null,
            author: null,
        };

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleFormChange = this.handleFormChange.bind(this);
        this.search = this.search.bind(this);
        this.updateTopic = this.updateTopic.bind(this);
        this.deleteTopic = this.deleteTopic.bind(this);
    }

    search = (e) => {
        e.preventDefault();

        this.setState({isLoading: true});

        const {search} = this.state;

        const data = {
            search: search
        };

        axios.post('/api/topics/search', data, { headers: authHeader() })
            .then(res => {
                const data = res.data;
                this.setState({topics: data, isLoading: false});
            })
            .catch(err => {
                console.log(err);
                this.setState({isLoading: false});
            })
    };

    handleChange = (e) =>  {
        e.preventDefault();
        this.state[e.target.name] = e.target.value;
        this.setState(
            (this.state.search === null || this.state.search === "") ?
                {disabled: true} :
                {disabled: false}
        );
    };

    handleFormChange = (e) =>  {
        e.preventDefault();
        this.state[e.target.name] = e.target.value;
    };

    handleSubmit = (e) => {
        e.preventDefault();
        this.setState({isLoading: true});
        switch(this.state.button) {
            case "delete":
                this.deleteTopic(e);
                break;
            case "save":
                this.updateTopic(e);
                break;
            default:
                this.setState({isLoading: false});
                break;
        }
        this.resetState();
    };

    resetState() {
        this.setState({
            id: null,
            name: null,
            title: null,
            description: null,
            tags: null,
            author: null,

            button: null
        });
    }

    deleteTopic = (e) => {
        const {id} = this.state;
        this.setState({isLoading: true});
        e.preventDefault();
        if(id) {
            axios.delete('/api/topics/' + this.state.id, { headers: authHeader() })
                .then(res => {
                    const newTopics = this.state.topics.filter(t => +t.id !== +id);
                    this.setState({isLoading: false, topics: newTopics});
                })
                .catch(err => {
                    this.setState({isLoading: false});
                    console.log(err);
                })
        }
    };

    updateTopic = (e) => {
        e.preventDefault();
        const {id, topics} = this.state;
        if(id) {
            this.setState({isLoading: true});
            const data = {
                name: this.state.name,
                title: this.state.title,
                description: this.state.description,
                tags: this.state.tags,
                author: this.state.author
            };
            axios.put('/api/topics/' + this.state.id, data, { headers: authHeader() })
                .then(res => {
                    const topic = res.data;
                    const newTopics = topics.filter(t=>+t.id!==id);
                    newTopics.push(topic);
                    this.setState({isLoading: false, topics: newTopics})
                })
                .catch(err => {
                    this.setState({isLoading: false});
                    console.log(err);
                })
        }
    };

    render() {
        const { currentUser, topics, isLoading, disabled } = this.state;

        if(isLoading)
            return <Loading/>;

        const columns = [{
            dataField: 'id',
            text: '#',
            headerStyle: () => {
                return {
                };
            }
        }, {
            dataField: 'name',
            text: 'Name',
            headerStyle: () => {
                return {
                };
            }
        }, {
            dataField: 'tags',
            text: 'Tags',
            headerStyle: () => {
                return {
                };
            }
        }, {
            dataField: 'author',
            text: 'Author       ',
            headerStyle: () => {
                return {
                };
            },
        }];

        const expandColumnRenderer = ({ expanded }) => {
            if (expanded) {
                return (
                    <b style={{borderRadius: "5px", border: "2px solid black", paddingRight: "5px", paddingLeft: "5px"}}>-</b>
                );
            }
            return <b style={{borderRadius: "5px", border: "2px solid black",paddingRight: "3px", paddingLeft: "3px"}}>+</b>;
        };
        const expandHeaderColumnRenderer = ({ isAnyExpands }) => {
            if (isAnyExpands) {
                return <b  style={{borderRadius: "5px", border: "2px solid black",paddingRight: "5px", paddingLeft: "5px"}}>-</b>;
            }
            return <b style={{borderRadius: "5px", border: "2px solid black", paddingRight: "3.5px", paddingLeft: "3.5px"}}>+</b>;
        };


        const expandRow = {

            showExpandColumn: true,
            expandByColumnOnly: true,
            expandHeaderColumnRenderer: expandHeaderColumnRenderer,
            expandColumnRenderer: expandColumnRenderer,

            renderer: row => (
                <Form onSubmit={this.handleSubmit}>
                    <Form.Group as={Row}>
                        <Form.Label column sm="2">
                            Name
                        </Form.Label>
                        <Col sm="10">
                            <Form.Control
                                type="text"
                                placeholder="Name"
                                name="name"
                                defaultValue={row.name}
                                onChange={this.handleFormChange}
                            />
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row}>
                        <Form.Label column sm="2">
                            Title
                        </Form.Label>
                        <Col sm="10">
                            <Form.Control
                                type="text"
                                placeholder="Title"
                                name="title"
                                defaultValue={row.title}
                                onChange={this.handleFormChange}
                            />
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row}>
                        <Form.Label column sm="2">
                            Description
                        </Form.Label>
                        <Col sm="10">
                            <Form.Control
                                name="description"
                                as="textarea"
                                rows="3"
                                defaultValue={row.description}
                                onChange={this.handleFormChange}
                            />
                        </Col>
                    </Form.Group>

                    <Form.Group as={Row}>
                        <Form.Label column sm="2">
                            Tags
                        </Form.Label>
                        <Col sm="10">
                            <Form.Control
                                type="text"
                                placeholder="Tags"
                                name="tags"
                                defaultValue={row.tags}
                                onChange={this.handleFormChange}
                            />
                        </Col>
                    </Form.Group>

                    <ButtonGroup>
                        <Button variant="primary" type="submit"
                                onClick={()=>this.setState({button: "save",author: row.author, id: row.id})}>
                            Save
                        </Button>
                        <Button variant="danger" type="submit"
                                onClick={()=>this.setState({button: "delete",author: row.author, id: row.id})}>
                            Delete
                        </Button>
                    </ButtonGroup>
                </Form>
            )};

        const rowEvents = {
            onClick: (e, row, rowIndex) => {
                this.props.history.push('/topic/' + row.id + '/public');
            }
        };


        return (
            <main role='main' className='mt-3 flex-shrink-0'>
                <div className="container pb-3">
                    <Form className="col" inline  onSubmit={this.search}>
                        <Form.Control placeholder="by <topic name> | <#tag> | <@author>" name="search" className="form-control col-10" type="text" onChange={this.handleChange}/>
                        <Button className="col-2" type={"submit"} disabled={disabled}>Search</Button>
                    </Form>
                </div>
                <div className="container">
                    <BootstrapTable
                        keyField='id'
                        bordered={false}
                        data={ topics.sort((a,b) => b.id - a.id) }
                        columns={ columns }
                        expandRow={ expandRow }
                        rowEvents={rowEvents}
                        noDataIndication="No content"
                    />
                </div>
            </main>
        );
    }
}

export default AdminTopics;