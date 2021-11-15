import React, { Component } from 'react';

import { Button, ButtonGroup, Form, Row, Col } from 'react-bootstrap';
import AuthService from "../../services/auth/AuthService";
import axios from 'axios';
import BootstrapTable from 'react-bootstrap-table-next';
import Loading from "../../blocks/Loading";

import authHeader from "../../services/auth/auth-header";

class AdminUsers extends Component {

    constructor(props) {
        super(props);
        this.state = {
            currentUser: AuthService.getCurrentUser(),
            isLoading: false,
            disabled: true,
            search: null,
            btn: null,
            id: null,

            users: [],
            user: null
        };

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleFormChange = this.handleFormChange.bind(this);
        this.search = this.search.bind(this);
    }

    handleFormChange = (e)=> {
        e.preventDefault();
        this.state[e.target.name] = e.target.value;
    };


    
    deleteUser = (e) => {
        const {id} = this.state;
        this.setState({isLoading: true});
        e.preventDefault();
        if(id) {
            axios.delete('/api/users/' + id, { headers: authHeader() })
                .then(res => {
                    const newUsers = this.state.users.filter(t => +t.id !== +id);
                    this.setState({isLoading: false, users: newUsers});
                })
                .catch(err => {
                    this.setState({isLoading: false});
                    console.log(err);
                })
        }
    };

    updateUser = (e) => {
        e.preventDefault();
        const {id, users} = this.state;
        if(id) {
            this.setState({isLoading: true});
            const data = {
                username: this.state.username,
                email: this.state.email
            };
            console.log(data);
            axios.put('/api/users/' + id, data, { headers: authHeader() })
                .then(res => {
                    const user = res.data;
                    console.log(user);
                    const newUsers = users.filter(t=>+t.id !== +id);
                    newUsers.push(user);
                    this.setState({isLoading: false, users: newUsers})
                })
                .catch(err => {
                    this.setState({isLoading: false});
                    console.log(err);
                })
        }
    };

    makeAdmin = (e) => {
        e.preventDefault();
        const {id, users} = this.state;
        if(id) {
            this.setState({isLoading: true});
            const data = {
                username: this.state.username,
                email: this.state.email
            };
            axios.patch('/api/users/' + id + "?roleName=ROLE_ADMIN", data, { headers: authHeader() })
                .then(res => {
                    const user = res.data;
                    const newUsers = users.filter(t=>+t.id!==+id);
                    newUsers.push(user);
                    this.setState({isLoading: false, users: newUsers})
                })
                .catch(err => {
                    this.setState({isLoading: false});
                    console.log(err);
                })
        }
    };

    makeUser = (e) => {
        e.preventDefault();
        const {id, users} = this.state;
        if(id) {
            this.setState({isLoading: true});
            const data = {
                username: this.state.username,
                email: this.state.email
            };
            axios.patch('/api/users/' + id + "?roleName=ROLE_USER", data, { headers: authHeader() })
                .then(res => {
                    const user = res.data;
                    const newUsers = users.filter(t=>+t.id!==+id);
                    newUsers.push(user);
                    this.setState({isLoading: false, users: newUsers})
                })
                .catch(err => {
                    this.setState({isLoading: false});
                    console.log(err);
                })
        }
    };




    search = (e) => {
        e.preventDefault();

        this.setState({isLoading: true});

        const {search} = this.state;

        const data = {
            search: search
        };
        axios.post('/api/users/search', data, { headers: authHeader() })
        .then(res => {
            const data = res.data;
           this.setState({users: data, isLoading: false});
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

    handleSubmit = (e) => {
        e.preventDefault();
        this.setState({isLoading: true});
        switch(this.state.button) {
            case "delete":
                this.deleteUser(e);
                break;
            case "save":
                this.updateUser(e);
                break;
            case "makeadmin":
                this.makeAdmin(e);
                break;
            case "makeuser":
                this.makeUser(e);
                break;
            default:
                this.setState({isLoading: false});
                break;
        }
        this.resetState();
    };

    resetState() {
        this.setState({
            isLoading: false,
            search: null,
            btn: null,
            id: null,
            button: null,
            user: null,
            username: null,
            password: null,
            email: null
        });
    }


    render() {
        const { currentUser, users, isLoading, disabled } = this.state;

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
            dataField: 'username',
            text: 'Username',
            headerStyle: () => {
                return {
                };
            }
        }, {
            dataField: 'email',
            text: 'Email',
            headerStyle: () => {
                return {
                };
            }
        }, {
            dataField: 'role',
            text: 'Roles       ',
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
            renderer: row => (
                    <div>
                    <Form onSubmit={this.handleSubmit}>
                    <Form.Group as={Row}>
                        <Form.Label column sm="2">
                            Username
                        </Form.Label>
                        <Col sm="10">
                            <Form.Control
                                name="username"
                                rows="3"
                                defaultValue={row["username"]}
                                onChange={this.handleFormChange}
                            />
                        </Col>
                        <Form.Label column sm="2">
                            Email
                        </Form.Label>
                        <Col sm="10">
                            <Form.Control
                                name="email"
                                rows="3"
                                defaultValue={row["email"]}
                                onChange={this.handleFormChange}
                            />
                        </Col>
                    </Form.Group>
                    <ButtonGroup>
                        <Button variant="primary" type="submit"
                                onClick={()=>this.setState({button: "save", id: row.id})}>
                            Save
                        </Button>
                        <Button variant="danger" type="submit"
                                onClick={()=>this.setState({button: "delete", id: row.id})}>
                            Delete
                        </Button>
                       {row["role"] === "ROLE_ADMIN" ? (
                        <Button variant="primary"
                                type="submit"
                                onClick={()=>this.setState({button: "makeuser", id: row.id})}>
                            Make user
                        </Button>
                        ) :
                        (
                        <Button variant="info" type="submit"
                                onClick={()=>this.setState({button: "makeadmin", id: row.id})}>
                            Make admin
                        </Button>
                        )
        }
                    </ButtonGroup>
                </Form>
                    </div>
            ),

            expandColumnRenderer: expandColumnRenderer
        };

        const rowEvents = {
            onClick: (e, row, rowIndex) => {
                this.props.history.push('#');
            }
        };


        return (
            <main role='main' className='mt-3 flex-shrink-0'>
                <div className="container pb-3">
                    <Form className="col" inline  onSubmit={this.search}>
                        <Form.Control placeholder="by <username> | <email>" name="search" className="form-control col-10" type="text" onChange={this.handleChange}/>
                        <Button className="col-2" type={"submit"} disabled={disabled}>Search</Button>
                    </Form>
                </div>
                <div className="container">
                    <BootstrapTable
                        keyField='id'
                        bordered={false}
                        data={ users.sort((a,b) => b.id - a.id) }
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

export default AdminUsers;