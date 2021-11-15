import React, {Component} from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

import CustomModal from "../blocks/CustomModal"
import Loading from "../blocks/Loading";

import { Button, ButtonGroup, Form, Row, Col } from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import AuthService from "../services/auth/AuthService";

import authHeader from "../services/auth/auth-header";

const XRegExp = require('xregexp');


import authHeader from '../services/auth/auth-header';

class Decks extends Component {

    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),

            decks: [],
            decksCopy: [],

            isLoading: false,
            id: null,
            name: null,
            del: false,
            subscribed: false,

            search: false
          };

        this.createDeck = this.createDeck.bind(this);
        this.updateDeck= this.updateDeck.bind(this);
        this.deleteDeck = this.deleteDeck.bind(this);
        this.handleChange= this.handleChange.bind(this);
        this.changeDeck = this.changeDeck.bind(this);
        this.goBack = this.goBack.bind(this);
        this.isSubscribed = this.isSubscribed.bind(this);
        this.search = this.search.bind(this);

        this.isSubscribed();
    }

    isSubscribed() {
        axios.get("/api/topics/"+this.props.match.params.id, {headers: authHeader()})
            .then(r=>{
                const data = r.data;
                this.state.subscribed = data && data.author !== this.state.currentUser.username;
            })
            .catch(err=>{
                console.log(err);
                this.state.subscribed = false;
            })
    }

    goBack(){
        this.props.history.goBack();
    }

    componentDidMount() {
        if(!this.state.currentUser) {
            this.props.history.push("/signin");
            return;
        }
        this.setState({ isLoading: true });
        axios.get('/api/topics/' + this.props.match.params.id + '/decks', { headers: authHeader() })
        .then(res => {
            const decks = res.data;
            this.setState({ decks: decks, isLoading: false });
        })
        .catch(err => {
            console.log(err);
        })
    }

    createDeck = (e) => {
        e.preventDefault();
        this.setState({isLoading: true});
        const data = {
            name: this.state.name,
            author: this.state.currentUser.username
        };
        axios.post('/api/decks/?topicId='+this.props.match.params.id, data, { headers: authHeader() })
            .then(res => {
                this.state.decks.push(res.data);
                this.setState({isLoading: false, decks: this.state.decks});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            });
    };

    handleChange = e => {
        e.preventDefault();
        this.setState({
            name: e.target.value,
            id: e.target.id
        });
    };

    changeDeck = e => {
        e.preventDefault();
        this.state.del ? this.deleteDeck(e) : this.updateDeck(e);
    };

    updateDeck = e => {
        e.preventDefault();
        if(this.state.id && this.state.name) {
            this.setState({ isLoading: true });
            const data = {
                name: this.state.name,
                author: this.state.currentUser.username
            };
            axios.put('/api/decks/'+this.state.id+'?topicId='+this.props.match.params.id, data, { headers: authHeader() })
                .then(res => {
                    const newDecks = this.state.decks.filter(d => +d.id !== +this.state.id);
                    newDecks.push(res.data);
                    this.setState({isLoading: false, decks: newDecks});
                })
                .catch(err => {
                    this.setState({isLoading: false});
                    console.log(err);
                })
        }
    };

    deleteDeck = e => {
        e.preventDefault();
        this.setState({ isLoading: true });
        axios.delete('/api/decks/'+this.state.id, { headers: authHeader() })
            .then(res => {
                const newDecks = this.state.decks.filter(d=>+d.id !== +this.state.id);
                this.setState({isLoading: false, decks: newDecks})
            })
            .catch(err => {
                this.setState({ isLoading: false });
                console.log(err);
            })
    };

    search(e) {
        e.preventDefault();

        let {decks, decksCopy, search} = this.state;
        const value = e.target.value;

        if(value === "") {
            this.setState({search: false, decks: decksCopy, decksCopy: [], isLoading: false});
            return;
        }
        if(!search) {
            this.setState({search: true, decksCopy: decks});
            decksCopy = decks;
        }

        let name= "";
        let  names = [];

        const letterTest = XRegExp("\\p{L}");
        const digitTest = XRegExp("\\d");

        for(let i = 0; i < value.length; i++) {
            let add = false;
            switch(value[i]) {
                case "@":
                    break;
                case "#":
                    break;
                default:
                    add = false;
                    for(; i < value.length; i++) {
                        let c = value[i];
                        if(letterTest.test(c) || digitTest.test(c)) {
                            name += c;
                            add = true;
                        } else {
                            if(c === " ") {
                                i--;
                            }
                            break;
                        }
                    }
                    if(add) {
                        names.push(name);
                        name = "";
                    }
                    break;
                case " ":
                    break;
                case "\n":
                    break;
            }
        }

        if(names.length !== 0) {
            this.setState({
                decks: [...new Set(
                    names
                        .map(n => decksCopy
                            .filter(deck => deck.name.toUpperCase().indexOf(n.toUpperCase()) !== -1)
                        )
                        .flat()
                )]
            });
        }

    }

    AddNewDeckForm() {
        return (
            <Form onSubmit={this.createDeck}>
                <Form.Group controlId="formBasicName">
                    <Form.Label>Deck name</Form.Label>
                    <Form.Control name="name" type="text" placeholder="Enter deck name" onChange={this.handleChange} />
                </Form.Group>
                <div className='text-center'>
                    <Button type="submit" variant="primary">Create deck</Button>
                </div>
            </Form>
        );
    }

    render() {
        const { currentUser, decks, isLoading, subscribed } = this.state;
        if(isLoading)
            return <Loading/>;

            const columns = [
                {
                    dataField: 'id',
                    text: '#'
                },
                {
                    dataField: 'name',
                    text: 'Name'
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

                <div>
                    {row.author === currentUser.username ?
                        <Form onSubmit={this.changeDeck}>
                            <Form.Group as={Row}>
                                <Form.Label column sm="2">
                                    Name
                                </Form.Label>
                                <Col sm="10">
                                    <Form.Control
                                        type="text"
                                        placeholder="Name"
                                        defaultValue={row.name}
                                        id={row.id}
                                        onChange={this.handleChange}
                                    />
                                </Col>
                            </Form.Group>

                            <ButtonGroup>
                                <Button variant="success" onClick={() => {this.props.history.push("/study/" + row.id)}}>
                                    Study
                                </Button>
                                <Button variant="primary" onClick={() => this.setState({del: false})} type="submit">
                                    Save
                                </Button>
                                <Button variant="danger" onClick={() => this.setState({del: true, id: row.id})} type="submit">
                                    Delete
                                </Button>
                            </ButtonGroup>
                        </Form> :
                        <div>Subscribed deck!</div>
                    }
                </div>
                )
            };

            const rowEvents = {
                onClick: (e, row, rowIndex) => {
                    this.props.history.push('/deck/' + row.id + '/cards');
                }
            };


                return (

                    <main role='main' className='mt-3 flex-shrink-0'>
                        <div className='container'>

                            <div className="mb-1">
                                {!subscribed ?
                                    <div className="row">
                                        <CustomModal className="col-2" name={"New deck"} title={"Add deck"} body={this.AddNewDeckForm()}/>
                                        <div className="col-1"/>
                                        <Form.Control className="col-9" type="text" placeholder="by <deck name>" onChange={this.search} />
                                    </div> :

                                    <div>
                                        <Form.Control type="text" placeholder="by <deck name>" onChange={this.search} />
                                    </div>
                                }
                            </div>

                            <BootstrapTable
                            keyField='id'
                            data={ decks.sort((a,b)=> b.id - a.id) }
                            columns={ columns }
                            expandRow={expandRow}
                            rowEvents={rowEvents}
                            bordered={false}
                            noDataIndication="No decks"
                            />
                            <br/>
                            <Link onClick={this.goBack} to={"#"}>◄ Go Back</Link>
                        </div>

                    </main>
                );
            }
}

export default Decks;
