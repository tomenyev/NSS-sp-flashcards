import React, {Component} from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import authHeader from '../services/auth/auth-header';

import CustomModal from "../blocks/CustomModal"
import Loading from "../blocks/Loading";

import { Button, ButtonGroup, Form, Row, Col } from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import AuthService from "../services/auth/AuthService";

import authHeader from "../services/auth/auth-header";

class Cards extends Component {

    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),

            cards: [],
            cardsCopy: [],

            isLoading: false,
            id: null,

            search: false,

            del: false,

            question: null,
            answer: null
          };

        this.createCard = this.createCard.bind(this);
        this.updateCard = this.updateCard.bind(this);
        this.deleteCard = this.deleteCard.bind(this);
        this.handleChange= this.handleChange.bind(this);
        this.changeCard = this.changeCard.bind(this);
        this.goBack = this.goBack.bind(this);
        this.search = this.search.bind(this);
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
        axios.get('/api/decks/' + this.props.match.params.id + '/cards', { headers: authHeader() })
        .then(res => {
            const cards = res.data;
            this.setState({ cards: cards, isLoading: false });
        })
        .catch(err => {
            console.log(err);
        })
    }

    createCard = (e) => {
        e.preventDefault();
        this.setState({isLoading: true});
        const data = {
            question: this.state.question,
            answer: this.state.answer
        };
        console.log(data);
        axios.post('/api/cards/?deckId='+this.props.match.params.id, data, { headers: authHeader() })
            .then(res => {
                this.state.cards.push(res.data);
                this.setState({isLoading: false, cards: this.state.cards});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            });
    };

    handleChange = e => {
        e.preventDefault();
        this.setState({
            question: e.target.value,
            id: e.target.id
        });
    };

    handleChange2 = e => {
        e.preventDefault();
        this.setState({
            answer: e.target.value,
            id: e.target.id
        });
    };

    changeCard = e => {
        e.preventDefault();
        this.state.del ? this.deleteCard(e) : this.updateCard(e);
    };

    updateCard = e => {
        e.preventDefault();
        this.setState({isLoading: true});
        const data = {
            question: this.state.question,
            answer: this.state.answer
        };
        axios.put('/api/cards/'+this.state.id+'?deckId='+this.props.match.params.id, data, { headers: authHeader() })
            .then(res => {
                const card = res.data;
                const newCards = this.state.cards.filter(c => c.id !== card.id);
                newCards.push(card);
                this.setState({isLoading: false, cards: newCards});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            })
    };

    deleteCard = e => {
        e.preventDefault();
        this.setState({ isLoading: true });
        axios.delete('/api/cards/'+this.state.id, { headers: authHeader() })
            .then(res => {
                const newCards = this.state.cards.filter(c => c.id !== this.state.id);
                this.setState({isLoading: false, cards: newCards});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            })
    };

    search(e) {
        e.preventDefault();

        let {cards, cardsCopy, search} = this.state;
        const value = e.target.value;

        if(value === "") {
            this.setState({search: false, cards: cardsCopy, cardsCopy: [], isLoading: false});
            return;
        }
        if(!search) {
            this.setState({search: true, cardsCopy: cards});
            cardsCopy = cards;
        }

        let name= "";
        let  names = [];

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
                        if(c === " ") {
                            i--;
                            break;
                        } else {
                            name += c;
                            add = true;
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
                cards: [...new Set(
                    names
                        .map(n => cardsCopy
                            .filter(card => card.question.toUpperCase().indexOf(n.toUpperCase()) !== -1)
                        )
                        .flat()
                )]
            });
        }

    }

    AddNewCardForm() {
        return (
            <Form onSubmit={this.createCard}>
                <Form.Group controlId="formBasicName">
                    <Form.Label>Question</Form.Label>
                    <Form.Control name="question" type="text" placeholder="Enter card question" onChange={this.handleChange} />
                    <Form.Label>Answer</Form.Label>
                    <Form.Control name="answer" type="text" placeholder="Enter card answer" onChange={this.handleChange2} />
                </Form.Group>
                <div className='text-center'>
                    <Button type="submit" variant="primary">Create card</Button>
                </div>
            </Form>
        );
    }

    render() {
        const { currentUser, cards, isLoading } = this.state;
        if(isLoading)
            return <Loading/>;

            const columns = [
                {
                    dataField: 'id',
                    text: '#'
                },
                {
                    dataField: 'question',
                    text: 'Question'
                },
                {
                    dataField: 'answer',
                    text: 'Answer'
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
                        <Form onSubmit={this.changeCard}>
                            <Form.Group as={Row}>
                                <Form.Label column sm="2">
                                    Question
                                </Form.Label>
                                <Col sm="10">
                                    <Form.Control
                                        type="text"
                                        placeholder="Question"
                                        defaultValue={row.question}
                                        id={row.id}
                                        onChange={this.handleChange}
                                    />
                                </Col>
                                <Form.Label column sm="2">
                                    Answer
                                </Form.Label>
                                <Col sm="10">
                                    <Form.Control
                                        type="text"
                                        placeholder="Answer"
                                        defaultValue={row.answer}
                                        id={row.id}
                                        onChange={this.handleChange2}
                                    />
                                </Col>
                            </Form.Group>

                            <ButtonGroup>
                                <Button variant="primary" onClick={() => this.setState({del: false})} type="submit">
                                    Save
                                </Button>
                                <Button variant="danger" onClick={() => this.setState({del: true, id: row.id})} type="submit">
                                    Delete
                                </Button>
                            </ButtonGroup>
                        </Form>
                    
                </div>
                )
            };

        


                return (

                    <main role='main' className='mt-3 flex-shrink-0'>
                        <div className='container'>

                            <div className="mb-1">
                                <div className="row">
                                    <ButtonGroup className="cal-3">
                                        <CustomModal name={"New card"} title={"Add card"} body={this.AddNewCardForm()}/>
                                        <Button variant="success" onClick={() => {this.props.history.push("/study/" + this.props.match.params.id)}}>
                                            Study
                                        </Button>
                                    </ButtonGroup>
                                    <div className="col-1"/>
                                    <Form.Control className="col-8" type="text" placeholder="by <card question>" onChange={this.search}/>
                                </div>
                            </div>

                            <BootstrapTable
                                keyField='id'
                                data={ cards.sort((a,b)=> b.id - a.id) }
                                columns={ columns }
                                expandRow={expandRow}
                                bordered={false}
                                noDataIndication="No cards"
                            />
                            <br/>
                            <Link onClick={this.goBack} to={"#"}>◄ Go Back</Link>
                        </div>

                    </main>
                );
            }
}

export default Cards;
