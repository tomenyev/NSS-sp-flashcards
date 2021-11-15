import React, { Component } from 'react';

import { Button, ButtonGroup, Form, Row, Col } from 'react-bootstrap';
import AuthService from "../services/auth/AuthService";
import axios from 'axios';
import BootstrapTable from 'react-bootstrap-table-next';
import Loading from "../blocks/Loading";

import authHeader from "../services/auth/auth-header";

class Search extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            isLoading: false,
            disabled: true,
            search: null,
            btn: null,
            id: null,
            topics: []
        };

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.search = this.search.bind(this);
    }

    componentDidMount() {
        if(!this.state.currentUser) {
            this.props.history.push("/signin");
        }
    }

    search = (e) => {
        e.preventDefault();

        this.setState({isLoading: true});
        const {search, currentUser} = this.state;
        const data = {
            search: search
        };

        axios.post('/api/topics/public', data, { headers: authHeader() })
            .then(res => {
                const data = res.data;
                axios.get('/api/users/' + currentUser.id + '/topics', { headers: authHeader() })
                    .then(r => {
                        const topics = r.data;
                        data.forEach(d => {
                            d.subscribed = !!topics.find(t =>(t.id === d.id));
                        });
                        this.setState({topics: data, isLoading: false});
                    })
                    .catch(err => {
                        this.setState({isLoading: false});
                        console.log(err);
                    });
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
        const {currentUser, id, btn} = this.state;
        axios.put('/api/users/' + currentUser.id + '/'+btn+"?topicId="+id, {},{ headers: authHeader() })
            .then(r => {
                if(btn === "subscribe") { this.changeSubscribe(id, true)}
                if(btn === "unsubscribe") {this.changeSubscribe(id, false)}
                this.setState({isLoading: false, topics: this.state.topics});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            });
    };

    changeSubscribe(id, sub) {
        this.state.topics.find(t => t.id === id).subscribed = sub;
    }

    render() {
        const { currentUser,topics, isLoading, disabled } = this.state;

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
                currentUser.username !== row.author ?
                    <div>
                        <Form onSubmit={this.handleSubmit}>
                            <ButtonGroup>
                                <Button variant="primary"
                                        type="submit"
                                        name="copy"
                                        onClick={(e)=>{this.setState({btn: "copy", id: row.id})}}
                                >
                                    Copy
                                </Button>
                                { !row.subscribed &&
                                <Button variant="danger"
                                        type="submit"
                                        name="subscribe"
                                        onClick={(e)=>{this.setState({btn: "subscribe", id: row.id})}}
                                >
                                    Subscribe
                                </Button>
                                }
                                { row.subscribed === true &&
                                <Button variant="secondary"
                                        type="submit"
                                        name="unsubscribe"
                                        onClick={(e)=>{this.setState({btn: "unsubscribe", id: row.id})}}
                                >
                                    Unsubscribe
                                </Button>
                                }
                            </ButtonGroup>
                        </Form>
                    </div> :
                    <div>Your topic!</div>
            )
        };

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

export default Search;