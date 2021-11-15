import React, { Component } from 'react';

import { Button, ButtonGroup, Form, Row, Col } from 'react-bootstrap';
import AuthService from "../../services/auth/AuthService";
import axios from 'axios';
import BootstrapTable from 'react-bootstrap-table-next';
import Loading from "../../blocks/Loading";

import authHeader from "../../services/auth/auth-header";

class AdminReviews extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            isLoading: false,
            disabled: true,
            search: null,
            btn: null,
            id: null,

            review: null,

            reviews: []
        };

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleFormChange = this.handleFormChange.bind(this);
        this.search = this.search.bind(this);
    }

    search = (e) => {
        e.preventDefault();

        this.setState({isLoading: true});

        const {search} = this.state;

        const data = {
            search: search
        };

        axios.post('/api/reviews/search', data, { headers: authHeader() })
            .then(res => {
                const data = res.data;
                this.setState({reviews: data, isLoading: false});
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
            isLoading: false,
            search: null,
            btn: null,
            id: null,
            button: null,
            review: null,
        });
    }

    deleteTopic = (e) => {
        const {id} = this.state;
        this.setState({isLoading: true});
        e.preventDefault();
        if(id) {
            axios.delete('/api/reviews/' + id, { headers: authHeader() })
                .then(res => {
                    const newReviews = this.state.reviews.filter(t => +t.id !== +id);
                    this.setState({isLoading: false, reviews: newReviews});
                })
                .catch(err => {
                    this.setState({isLoading: false});
                    console.log(err);
                })
        }
    };

    updateTopic = (e) => {
        e.preventDefault();
        const {id, reviews} = this.state;
        if(id) {
            this.setState({isLoading: true});
            const data = {
                review: this.state.review
            };
            axios.put('/api/reviews/' + id, data, { headers: authHeader() })
                .then(res => {
                    const review = res.data;
                    const newReviews = reviews.filter(t=>+t.id!==id);
                    newReviews.push(review);
                    this.setState({isLoading: false, reviews: newReviews})
                })
                .catch(err => {
                    this.setState({isLoading: false});
                    console.log(err);
                })
        }
    };

    handleFormChange = (e)=> {
        e.preventDefault();
        this.state[e.target.name] = e.target.value;
    };


    render() {
        const { currentUser, reviews, isLoading, disabled } = this.state;

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
            dataField: 'author',
            text: 'Author',
            headerStyle: () => {
                return {
                };
            }
        }, {
            dataField: 'review',
            text: 'Review',
            headerStyle: () => {
                return {
                };
            }
        }, {
            dataField: 'rate',
            text: 'Rate       ',
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
                            Review
                        </Form.Label>
                        <Col sm="10">
                            <Form.Control
                                name="review"
                                rows="3"
                                defaultValue={row["review"]}
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
                    </ButtonGroup>
                </Form>
            )
        };

        const rowEvents = {
            onClick: (e, row, rowIndex) => {
                this.props.history.push('/topic/'+row.topic+'/public#reviews');
            }
        };

        return (
            <main role='main' className='mt-3 flex-shrink-0'>
                <div className="container pb-3">
                    <Form className="col" inline  onSubmit={this.search}>
                        <Form.Control placeholder="by <@author> | <description>" name="search" className="form-control col-10" type="text" onChange={this.handleChange}/>
                        <Button className="col-2" type={"submit"} disabled={disabled}>Search</Button>
                    </Form>
                </div>
                <div className="container">
                    <BootstrapTable
                        keyField='id'
                        bordered={false}
                        data={ reviews.sort((a,b) => b.id - a.id) }
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

export default AdminReviews;