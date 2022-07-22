import React from "react";
import {Background, Content, CloseModalButton, ModalWrapper} from './Modal.elements';

const Modal = ({ isOpen, close, children }) => {
    if (!isOpen) {
        return null;
    }

    return <Background onClick= {close}>
        <ModalWrapper>
        <CloseModalButton aria-label="Close modal" onClick={close} />
            <Content>{children}
            </Content>
        </ModalWrapper>
    </Background>;
};

export default Modal;