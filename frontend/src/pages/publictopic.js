/* eslint-disable */
import React, { Component } from 'react';

import AuthService from "../services/auth/AuthService";
import axios from 'axios';
import Loading from "../blocks/Loading";

import authHeader from "../services/auth/auth-header";
import {Button, Card, CardColumns, Form, Nav, ListGroup, CardGroup} from "react-bootstrap";
import CustomModal from "../blocks/CustomModal";
import {Link} from "react-router-dom";

class PublicTopic extends Component {

    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            isLoading: false,
            topic: null,
            reviews: [],

            likes: 0,
            dislikes: 0,

            like: false,
            reviewContent: null,
            subscribed: false
        };

        this.goBack = this.goBack.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.createReview = this.createReview.bind(this);
    }

    componentDidMount() {
        if(!this.state.currentUser) {
            this.props.history.push("/signin");
            return;
        }
        this.setState({ isLoading: true });
        axios.get('/api/topics/' + this.props.match.params.id, { headers: authHeader() })
            .then(res => {
                const topic = res.data;
                axios.get('/api/topics/'+topic.id+"/reviews", { headers: authHeader() })
                    .then( res =>{
                        const reviews = res.data;
                        if(topic.author !== this.state.currentUser.username) {
                            axios.get('/api/users/' + this.state.currentUser.id + '/topics', { headers: authHeader() })
                                .then(res => {
                                    const topics = res.data;
                                    const likes = reviews.filter(r=>r.like === true).length;
                                    this.setState({
                                        topic: topic,
                                        reviews: reviews,
                                        isLoading: false,
                                        subscribed: !!topics.find(t=>t.id === topic.id),
                                        likes: likes,
                                        dislikes: (reviews.length - likes)
                                    });
                                })
                                .catch(err => {
                                    console.log(err);
                                    this.setState({ isLoading: false });
                                });
                        } else {
                            const likes = reviews.filter(r=>r.like === true).length;
                            this.setState({
                                topic: topic,
                                reviews: reviews,
                                isLoading: false,
                                likes: likes,
                                dislikes: (reviews.length - likes)
                            });
                        }
                    })
                    .catch(err => {
                        console.log(err);
                        this.setState({ isLoading: false });
                    })
            })
            .catch(err => {
                console.log(err);
                this.setState({ isLoading: false });
            });
    }

    handleClick = e => {
        e.preventDefault();
        this.setState({isLoading: true});
        const {currentUser, topic} = this.state;
        const btn = e.target.name;
        axios.put('/api/users/' + currentUser.id + '/'+btn+"?topicId="+topic.id, {}, { headers: authHeader() })
            .then(r => {
                let subscribed = false;
                if(btn === "subscribe") { subscribed = true;}
                if(btn === "unsubscribe") { subscribed = false; }
                this.setState({isLoading: false, topic: this.state.topic, subscribed: subscribed});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            });
    };

    createReview = e => {
        e.preventDefault();
        this.setState({isLoading: true});
        const {currentUser, reviewContent, like, topic, reviews} = this.state;
        const data = {
            author: currentUser.username,
            review: reviewContent,
            rate: like
        };
        axios.post('/api/reviews/?userId=' + currentUser.id + "&topicId="+topic.id, data, { headers: authHeader() })
            .then(r => {
                reviews.push(r.data);
                this.setState({isLoading: false, reviews: reviews, like: false});
            })
            .catch(err => {
                this.setState({isLoading: false, like: false});
                console.log(err);
            });
    };

    handleChange = e => {
        e.preventDefault();
        this.state[e.target.name] = e.target.value;
    };

    goBack(){
        this.props.history.goBack();
    }

    AddReviewForm() {
        return (
            <Form onSubmit={this.createReview}>
                <Form.Group>
                    <Form.Label>Review content</Form.Label>
                    <Form.Control as="textarea" name="reviewContent" onChange={this.handleChange} rows="3" />
                </Form.Group>
                <Form.Check
                    type="switch"
                    id="custom-switch"
                    label="Like"
                    onChange={()=>{this.state.like = !this.state.like}}
                />
                <div className='text-center'>
                    <Button type="submit" variant="primary">Add review</Button>
                </div>
            </Form>
        );
    };

    render() {
        const {currentUser, isLoading, reviews, topic, subscribed, likes, dislikes} = this.state;
        if(isLoading || !topic ) {
            return <Loading/>;
        }
        return (
            <div className={"container"}>
                <Link onClick={this.goBack} to={"#"}>◄ Go Back</Link>
                <Card>
                    <Card.Header>
                        <Nav variant="pills" defaultActiveKey="#first">
                            <Nav.Item>
                                <Nav.Link href="#topic" className={this.props.location.hash !== "#reviews" ? "active" : ""}>Topic</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link href="#reviews" className={this.props.location.hash === "#reviews" ? "active" : ""}>Reviews</Nav.Link>
                            </Nav.Item>
                        </Nav>
                    </Card.Header>
                    <Card.Body>
                        {
                            this.props.location.hash === "#reviews" ?
                                <CardColumns>
                                    {reviews
                                        .sort((a,b) => b.id - a.id)
                                        .map(r => {
                                            return (
                                                <Card key={r.id} border={r.rate === true ? "success" : "danger"}>
                                                    <Card.Header>{r.author}</Card.Header>
                                                    <Card.Body>
                                                        <Card.Text>
                                                            {r.review}
                                                        </Card.Text>
                                                    </Card.Body>
                                                </Card>
                                            );
                                        })}
                                </CardColumns> :
                                <ListGroup variant="flush">
                                    <ListGroup.Item style={{borderBottom: "none"}}>
                                        <Card>
                                            <Card.Header>
                                                Title
                                            </Card.Header>
                                            <Card.Body>
                                                {topic.title}
                                            </Card.Body>
                                        </Card>
                                    </ListGroup.Item >
                                    <ListGroup.Item style={{borderBottom: "none"}}>
                                        <Card>
                                            <Card.Header>
                                                Tags
                                            </Card.Header>
                                            <Card.Body>
                                                {topic.tags}
                                            </Card.Body>
                                        </Card>
                                    </ListGroup.Item>
                                    <ListGroup.Item style={{borderBottom: "none"}}>
                                        <Card>
                                            <Card.Header>
                                                Description
                                            </Card.Header>
                                            <Card.Body>
                                                {topic.description}
                                            </Card.Body>
                                        </Card>
                                    </ListGroup.Item>
                                    <ListGroup.Item style={{borderBottom: "none"}}>
                                        <CardGroup>
                                            <Card style={{textAlign: "center"}}>
                                                <Card.Header>
                                                    likes
                                                </Card.Header>
                                                <Card.Body>
                                                    {likes}
                                                </Card.Body>
                                            </Card>
                                            <Card style={{textAlign: "center"}}>
                                                <Card.Header>
                                                    dislikes
                                                </Card.Header>
                                                <Card.Body>
                                                    {dislikes}
                                                </Card.Body>
                                            </Card>
                                            <Card style={{textAlign: "center"}}>
                                                <Card.Header>
                                                    author
                                                </Card.Header>
                                                <Card.Body>
                                                    {topic.author}
                                                </Card.Body>
                                            </Card>
                                        </CardGroup>
                                    </ListGroup.Item>
                                </ListGroup>
                        }
                    </Card.Body>
                    {
                        this.props.location.hash !== "#reviews" &&
                        <Card.Footer>
                            <div className="btn-group">
                                { currentUser.username !== topic.author && ( subscribed ?
                                    <Button variant="secondary" name="unsubscribe" onClick={this.handleClick}>Unsubscribe</Button> :
                                    <Button variant="danger" name="subscribe" onClick={this.handleClick}>Subscribe</Button>)
                                }
                                <Button variant="info" name="copy" onClick={this.handleClick}>Copy</Button>
                                <CustomModal name={"Add review"} title={"Add review"} body={this.AddReviewForm()}/>
                            </div>
                        </Card.Footer>
                    }
                </Card>
            </div>
        );
    }
}

export default PublicTopic;