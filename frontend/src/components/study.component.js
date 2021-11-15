import React, { Component } from "react";

import AuthService from "../services/auth/AuthService";

import Card from '../components/card.component';

import axios from 'axios';
import Loading from "../blocks/Loading";



import { Link } from 'react-router-dom';

import authHeader from "../services/auth/auth-header";


class Study extends Component {
    constructor(props) {
      super(props);
      this.state = {
        cardNumber: 0,
        cards: [],
        deckId: props.deckId,

        finish: false,
      };
      this.boundCallback = this.hideCreateCard.bind(this);
      this.boundCreateCard = this.setCard.bind(this);
      this.boundShowPrevCard = this.showPrevCard.bind(this);
      this.boundShowNextCard = this.showNextCard.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true, finish: false});
         
            axios.get('/api/decks/' + this.state.deckId + '/cards', { headers: authHeader() })
            .then(res => {
                const cards = res.data;
                this.setState({ cards: cards, isLoading: false });
            })
            .catch(err => {
                console.log(err);
            })
        
    }
    
    hideCreateCard() {
      this.setState({showModal: false});
    }
    
    showNextCard() {
      if ((this.state.cardNumber + 1) !== this.state.cards.length) {
        this.setState({cardNumber: this.state.cardNumber += 1});
      } else {
        this.setState({finish: true});
      }
      
    }
    
    showPrevCard() {
      if (this.state.cardNumber !== 0) {
        this.setState({cardNumber: this.state.cardNumber -= 1});
      }
    }
    
    setCard(card) {
      const newCards = this.state.cards.push(card);
      this.setState({cards: newCards});
    }
    
    // generateDots() {
    //   const times = this.state.cards.size;
    //   let arr = [];
    //   times.forEach((num) => {
    //     const dotClass = num  === this.state.cardNumber ? 'active' : '';
    //     arr.push(
    //       <span 
    //         className={`card-container__dot fa fa-circle ${dotClass}`}
    //         onClick={() => this.setState({cardNumber: num})}
    //       />
    //     )
    //   });
    //   return arr;
    // }
    
    generateCards() {
      const cards = this.state.cards;
       const cardsList = cards.map((card) => {
          return (
            <Card 
              frontContent={card.question}
              backContent={card.answer}
              showNextCard={this.boundShowNextCard}
              showPrevCard = {this.boundShowPrevCard}
              cardNumber={this.state.cardNumber}
            />
            );
        })
    
       return(
           cards.length <= 0 ?
            (<span>No cards in this deck :( <a href="/">Go Back?</a></span>)
           : (
           !this.state.finish ?
           cardsList[this.state.cardNumber]
           :
           <span>Good! <Link to={'/study/' + this.state.deckId} onClick={() => this.setState({finish: false, cardNumber: 0})}>Try again?</Link></span>
           )
           ); 
    }
    render() {
      return (
        <div>
            
          {this.generateCards()}
          <div className='card-container__dots-wrapper'>
            {/* {this.generateDots()} */}
          </div>
        </div>
     );
    }
  }
  export default Study;