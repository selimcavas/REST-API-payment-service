import styled from "styled-components";
import { MdClose } from "react-icons/md";

export const Background = styled.div`
width: 100vw;
height: 100vh;
background-color: rgba(255, 255, 255, 0.9);
position: fixed;
top: 0;
left: 0;
display: flex;
justify-content: center;
align-items: center;
`;

export const ModalWrapper = styled.div`
width: 50%;
height: 100px;
box-shadow: 0 0px 3px 2px rgba(0, 0, 0, 0.2);
background: #fff;
color: #000;
position: relative;
z-index: 10;
border-radius: 10px;
`;

export const Content = styled.div`
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;
padding: 20px;
`;


export const CloseModalButton = styled(MdClose)`
cursor: pointer;
position: absolute;
top: 10px;
right: 10px;
width: 30px;
height: 30px;
padding: 0;
z-index: 10;
`;
