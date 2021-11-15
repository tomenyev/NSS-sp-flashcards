import React, { Component } from 'react';

import { Button, ButtonGroup, Form, Row, Col } from 'react-bootstrap';
import AuthService from "../services/auth/AuthService";
import axios from 'axios';
// import { Link } from 'react-router-dom';
import BootstrapTable from 'react-bootstrap-table-next';
import Loading from "../blocks/Loading";

import authHeader from "../services/auth/auth-header";

import Dropzone from "react-dropzone";

import '../styles/style.css';

const XRegExp = require('xregexp');

class Topics extends Component {

    constructor(props) {
        super(props);
        this.state = {
            currentUser: AuthService.getCurrentUser(),
            topics: [],
            topicsCopy: [],
            isLoading: false,
            enter: false,

            id: null,
            name: null,
            title: null,
            description: null,
            tags: null,
            author: null,

            search: false,
            button: null
          };

        this.deleteTopic = this.deleteTopic.bind(this);
        this.updateTopic = this.updateTopic.bind(this);
        this.shareTopic = this.shareTopic.bind(this);
        this.unshareTopic = this.unshareTopic.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.resetState = this.resetState.bind(this);
        this.unsubscribeTopic = this.unsubscribeTopic.bind(this);
        this.exportTopic = this.exportTopic.bind(this);
        this.onDrop = this.onDrop.bind(this);
        this.onEnter = this.onEnter.bind(this);
        this.onLeave = this.onLeave.bind(this);
        this.fileUpload = this.fileUpload.bind(this);
        this.search = this.search.bind(this);
    }

    componentDidMount() {
        if(!this.state.currentUser) {
            this.props.history.push("/signin");
            return;
        }
        this.setState({ isLoading: true });
        axios.get('/api/users/' + this.state.currentUser.id + '/topics', { headers: authHeader() })
        .then(res => {
            const topics = res.data;
            this.setState({ topics: topics, isLoading: false });
        })
        .catch(err => {
            console.log(err);
        });
    }

    handleChange = e => {
        this.setState({ [e.target.name]: e.target.value });
    };

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

    shareTopic = (e) => {
        e.preventDefault();
        const {id, topics} = this.state;
        this.setState({isLoading: true});
        axios.put('/api/topics/' + id, {shared: true}, {headers: authHeader()})
            .then(res => {
                topics.find(t=>+t.id === id).shared = true;
                this.setState({isLoading: false, topics: topics});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            })
    };

    unshareTopic = (e) => {
        e.preventDefault();
        const {id, topics} = this.state;
        this.setState({isLoading: true});
        axios.put('/api/topics/' + id, {shared: false}, {headers: authHeader()})
            .then(res => {
                topics.find(t=>+t.id === id).shared = false;
                this.setState({isLoading: false, topics: topics});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            })
    };

    unsubscribeTopic = (e) => {
        e.preventDefault();
        this.setState({isLoading: true});
        const {currentUser, id, topics} = this.state;
        axios.put('/api/users/' + currentUser.id + "/unsubscribe/?topicId="+id, {}, { headers: authHeader() })
            .then(r => {
                this.setState({isLoading: false, topics: topics.filter(t => t.id !== id)});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            });
    };

    exportTopic = (e) => {
        e.preventDefault();
        this.setState({isLoading: true});
        const {currentUser, id} = this.state;
        axios.get('/api/users/' + currentUser.id + "/export/?topicId="+id, { headers: authHeader() })
            .then(r => {
                const text = r.data;
                this.download("topics"+id+".json", text);
                this.setState({isLoading: false});
            })
            .catch(err => {
                this.setState({isLoading: false});
                console.log(err);
            });
    };

    download(filename, text) {
        const element = document.createElement('a');
        element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
        element.setAttribute('download', filename);
        element.style.display = 'none';
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);
    }

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
            case "shared":
                this.shareTopic(e);
                break;
            case "unshare":
                this.unshareTopic(e);
                break;
            case "export":
                this.exportTopic(e);
                break;
            case "unsubscribe":
                this.unsubscribeTopic(e);
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

    onDrop(acceptedFiles, rejectedFiles) {
        this.state.topics.shift();
        this.setState({enter: false, topics: this.state.topics, isLoading: true});
        const {topics} = this.state;
        acceptedFiles.forEach((file)=> {
            this.fileUpload(file)
                .then((response)=>{
                    const topic = response.data;
                    topics.push(topic);
                    this.setState({isLoading: false, topics: topics})
                })
                .catch(err=>{
                    console.log(err);
                    this.setState({isLoading: false})
                });
        });
    }

    fileUpload(file){
        const {currentUser} = this.state;
        const formData = new FormData();
        formData.append('file',file);
        return  axios.post(
            '/api/users/'+currentUser.id+'/import',
            formData,
            { headers: {
                    Authorization: authHeader().Authorization,
                    'content-type': 'multipart/form-data'
            }})
    }

    onEnter(e) {
        e.preventDefault();
        if(!this.state.enter) {
            const topic = {id: "Your", name: "topic will", tags: "appear here", author: "after drop! =)"};
            this.state.topics.unshift(topic);
            this.setState({enter: true, topics: this.state.topics});
        }
    }

    onLeave(e) {
        e.preventDefault();
        this.state.topics.shift();
        this.setState({enter: false, topics: this.state.topics});
    }

    search(e) {
        e.preventDefault();

        let {topics, topicsCopy, search} = this.state;
        const value = e.target.value;

        if(value === "") {
            this.setState({search: false, topics: topicsCopy, topicsCopy: [], isLoading: false});
            return;
        }
        if(!search) {
            this.setState({search: true, topicsCopy: topics});
            topicsCopy = topics;
        }

        let tag = "", name= "", author= "";
        let tags = [], names = [], authors = [];

        const letterTest = XRegExp("\\p{L}");
        const digitTest = XRegExp("\\d");
        for(let i = 0; i < value.length; i++) {
            let add = false;
            switch(value[i]) {
                case "@":
                    for(++i; i < value.length; i++) {
                        let c = value[i];
                        if(letterTest.test(c) || digitTest.test(c)) {
                            author+=c;
                            add = true;
                        } else {
                            if(c === " ") {
                                --i;
                            }
                            break;
                        }
                    }
                    if(add) {
                        authors.push(author);
                        author = "";
                    }
                    break;
                case "#":
                    add = false;
                    for(++i; i < value.length; i++) {
                        let c = value[i];
                        if(letterTest.test(c) || digitTest.test(c)) {
                            tag += c;
                            add = true;
                        } else {
                            if(c === " ") {
                                --i;
                            }
                            break;
                        }
                    }
                    if(add) {
                        tags.push(tag);
                        tag = "";
                    }
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
                                --i;
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


        if(authors.length !== 0 && tags.length === 0 && names.length === 0) {
            this.setState({
                topics: [...new Set(
                    authors
                        .map(a => topicsCopy
                                .filter(topic => topic.author.toUpperCase().indexOf(a.toUpperCase()) !== -1)
                        )
                        .flat()
                )]
            });
            return;
        }

        if(authors.length === 0 && tags.length !== 0 && names.length === 0) {
            this.setState({
                topics: [...new Set(
                    tags
                        .map(t => topicsCopy
                                .filter(topic => topic.tags.toUpperCase().indexOf(t.toUpperCase()) !== -1)
                        )
                        .flat()
                )]
            });
            return;
        }

        if(authors.length === 0 && tags.length === 0 && names.length !== 0) {
            this.setState({
                topics: [...new Set(
                    names
                        .map(n => topicsCopy
                                .filter(topic => topic.name.toUpperCase().indexOf(n.toUpperCase()) !== -1)
                        )
                        .flat()
                )]
            });
            return;
        }

        if(authors.length !== 0 && tags.length !== 0 && names.length === 0) {
            this.setState({
                topics:[...new Set(
                    authors
                        .map(a => tags
                            .map(t => topicsCopy.filter(topic =>
                                topic.author.toUpperCase().indexOf(a.toUpperCase()) !== -1 &&
                                topic.tags.toUpperCase().indexOf(t.toUpperCase()) !== -1
                            ))
                            .flat()
                        )
                        .flat()
                )]
            });
            return;
        }

        if(authors.length === 0 && tags.length !== 0 && names.length !== 0) {
            this.setState({
                topics: [...new Set(
                    names
                        .map(n => tags
                            .map(t => topicsCopy.filter(topic =>
                                topic.name.toUpperCase().indexOf(n.toUpperCase()) !== -1 &&
                                topic.tags.toUpperCase().indexOf(t.toUpperCase()) !== -1
                            ))
                            .flat()
                        )
                        .flat()
                )]
            });
            return;
        }

        if(authors.length !== 0 && tags.length === 0 && names.length !== 0) {
            this.setState({
                topics: [...new Set(
                    authors
                        .map(a => names
                            .map(n => topicsCopy.filter(topic =>
                                topic.author.toUpperCase().indexOf(a.toUpperCase()) !== -1 &&
                                topic.name.toUpperCase().indexOf(n.toUpperCase()) !== -1
                            ))
                            .flat()
                        )
                        .flat()
                )]
            });
            return;
        }

        if(authors.length !== 0 && tags.length !== 0 && names.length !== 0) {
            this.setState({
                topics: [...new Set(
                    authors
                        .map(a => tags
                            .map(t => names
                                .map(n => topicsCopy.filter(topic =>
                                    topic.author.toUpperCase().indexOf(a.toUpperCase()) !== -1 &&
                                    topic.tags.toUpperCase().indexOf(t.toUpperCase()) !== -1 &&
                                    topic.name.toUpperCase().indexOf(n.toUpperCase()) !== -1
                                ))
                                .flat()
                            )
                            .flat()
                        )
                        .flat()
                )]
            });
        }

    }

    render() {
        const { currentUser, topics, enter, isLoading } = this.state;

        if(isLoading) {
            return <Loading/>;
        }

        const columns = [{
            dataField: 'id',
            text: '#',
            headerClasses: enter ? "blur borderedBottom" : ""
          }, {
            dataField: 'name',
            text: 'Name',
            headerClasses: enter ? "blur borderedBottom" : ""
          }, {
            dataField: 'tags',
            text: 'Tags',
            headerClasses: enter ? "blur borderedBottom" : ""
          }, {
            dataField: 'author',
            text: 'Author       ',
            headerClasses: enter ? "blur borderedBottom" : ""
          }];

        const expandColumnRenderer = ({ expanded }) => {
            if (expanded) {
                return (
                    <b className={enter ? "blur" : ""} style={{borderRadius: "5px", border: "2px solid black", paddingRight: "5px", paddingLeft: "5px"}}>-</b>
                );
            }
            return <b className={enter ? "blur" : ""} style={{borderRadius: "5px", border: "2px solid black",paddingRight: "3px", paddingLeft: "3px"}}>+</b>;
        };

        const expandHeaderColumnRenderer = ({ isAnyExpands }) => {
            if (isAnyExpands) {
                return <b className={enter ? "blur" : ""}  style={{borderRadius: "5px", border: "2px solid black",paddingRight: "5px", paddingLeft: "5px"}}>-</b>;
            }
            return <b className={enter ? "blur" : ""} style={{borderRadius: "5px", border: "2px solid black", paddingRight: "3.5px", paddingLeft: "3.5px"}}>+</b>;
        };

        const expandRow = {

            showExpandColumn: true,
            expandByColumnOnly: true,
            expandHeaderColumnRenderer: expandHeaderColumnRenderer,
            expandColumnRenderer: expandColumnRenderer,

            renderer: row => (
                <div className={enter ? "blur" : ""}>
                {row.author === currentUser.username ?
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
                                onChange={this.handleChange}
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
                                onChange={this.handleChange}
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
                                onChange={this.handleChange}
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
                                onChange={this.handleChange}
                            />
                        </Col>
                    </Form.Group>

                    <ButtonGroup>
                        <Button variant="primary" type="submit"
                                onClick={()=>this.setState({button: "save", author: row.author, id: row.id})}>
                            Save
                        </Button>
                        { !row.shared &&
                            <Button variant="success" type="submit"
                                    onClick={()=>this.setState({button: "shared", author: row.author, id: row.id})}>
                                Share
                            </Button>
                        }
                        { row.shared === true &&
                        <Button variant="warning" type="submit"
                                onClick={()=>this.setState({button: "unshare", author: row.author, id: row.id})}>
                            Unshare
                        </Button>
                        }
                        <Button
                            variant="info"
                            type="submit"
                            name="export"
                            onClick={(e)=>this.setState({button: "export", id: row.id})}
                        >
                            Export
                        </Button>
                        <Button variant="danger" type="submit"
                                onClick={()=>this.setState({button: "delete", author: row.author, id: row.id})}>
                            Delete
                        </Button>
                    </ButtonGroup>
                </Form> :
                    <Form onSubmit={this.handleSubmit}>
                        <ButtonGroup>
                            <Button
                                variant="info"
                                type="submit"
                                name="export"
                                onClick={(e)=>this.setState({button: "export", id: row.id})}
                            >
                                Export
                            </Button>
                            <Button variant="secondary"
                                    type="submit"
                                    name="unsubscribe"
                                    onClick={(e)=>{this.setState({button: "unsubscribe", id: row.id})}}
                            >
                                Unsubscribe
                            </Button>
                        </ButtonGroup>
                    </Form>
             }

            </div>
            )
        };

        const rowEvents = {
            onClick: (e, row, rowIndex) => {
                this.props.history.push('/topic/' + row.id + '/decks');
            }
        };

        return (
            <main role='main' className='mt-3 flex-shrink-0'>
                <Dropzone
                    noClick={true}
                    multiple={true}
                    preventDropOnDocument={true}
                    canCancel={false}
                    onDrop={this.onDrop}
                    accept={[".txt", ".json"]}
                    onDragEnter={this.onEnter}
                    onDragLeave={this.onLeave}
                >
                    {
                        ({ getRootProps, getInputProps }) => (
                            <div {...getRootProps({ className: "dropzone" })}>
                                <input {...getInputProps()} />
                                <div className={!enter ? "container" : "container bordered"}  >
                                    <div className={"container"} >
                                        <Form.Control className='mb-1' type="text" placeholder="by <topic name> | <#tag> | <@author>" onChange={this.search} />
                                        <BootstrapTable
                                            keyField='id'
                                            bordered={false}
                                            data={ topics.sort((a,b) => b.id - a.id) }
                                            columns={ columns }
                                            expandRow={ expandRow }
                                            rowClasses={(row, rowIndex) => enter && rowIndex !== 0 ? "blur" : ""}
                                            rowEvents={ rowEvents }
                                            noDataIndication="No topics"
                                        />
                                    </div>
                                </div>
                            </div>
                        )
                    }
                </Dropzone>
            </main>
        );
    }
}

export default Topics;
