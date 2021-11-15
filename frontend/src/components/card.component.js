import React, { Component } from 'react';

import { Table, Button, ButtonGroup, ListGroup, ListGroupItem } from 'react-bootstrap';
import AuthService from "../services/auth/AuthService";
import axios from 'axios';
import { Link } from 'react-router-dom';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

class Card extends Component {

    constructor(props) {
        super(props);
        this.state = {
            currentUser: AuthService.getCurrentUser(),
            card: null,
            isLoading: false,
            showAnswer: false
          };
    }



    componentDidMount() {
        this.setState({ isLoading: true });
   
    }

    render() {
        const content = this.state.showAnswer ? this.props.backContent : this.props.frontContent;
        const iconClass = this.state.showAnswer ? 'reply' : 'share';
        const cardClass = this.state.showAnswer ? 'back' : '';
        const contentClass = this.state.showAnswer ? 'back' : 'front';
        const actionClass = this.state.showAnswer ? 'active' : '';

        const card = {
            width: "30em",
            height: "15em",
            display: "flex",
            alignItems: "center",
            background: "white",
            boxShadow: "2px 3px 23px rgba(0, 0, 0, 0.1)",
            padding: "1em 3em",
            transformStyle: "preserve-3d",
            marginTop: "4em",
            transition: "0.2"
        };
        return (
                <div
                    className={`card ${cardClass}`}
                    onClick={() => this.setState({showAnswer: !this.state.showAnswer})}
                    style={card}
                >
                    <span className='card__counter'>{this.props.cardNumber + 1}</span>
                    <div
                        className='card__flip-card'
                        onClick={ () => {
                            this.setState({showAnswer: !this.state.showAnswer});
                        }}
                    >
                    </div>
                    <div className={`card__content--${contentClass}`}>
                        {content}
                    </div>
                    <div className={`card__actions ${actionClass}`}>
                        <div
                            className='card__prev-button'
                            onClick={() => {
                                this.props.showPrevCard();
                                this.setState({showAnswer: false});
                            }}
                        >
                            Prev
                        </div>
                        <div
                            className='card__next-button'
                            onClick={() => {
                                this.props.showNextCard();
                                this.setState({showAnswer: false});
                            }}
                        >
                            Next
                        </div>
                    </div>
                </div>
        )
    }
}

export default Card;
