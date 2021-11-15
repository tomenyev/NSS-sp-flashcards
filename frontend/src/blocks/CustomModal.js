import React, {useState} from "react";
import {Button} from "react-bootstrap";
import Modal from "react-bootstrap/Modal";

function CustomModal({name, title, body}) {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
            <Button variant="primary" onClick={handleShow}>
                {name}
            </Button>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {body}
                </Modal.Body>
            </Modal>
        </>
    );
}

export default CustomModal;