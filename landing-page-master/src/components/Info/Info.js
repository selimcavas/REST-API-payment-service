import React, {useState} from 'react';
import { Button, Container, StyledTextArea} from '../../globalStyles';
import { InfoSec, InfoColumn, TextWrap, ImgWrap, Img, ErrorWrap} from './Info.elements';
import Modal from './Modal';
import svg from '../../images/stars.svg';
import axios from 'axios';


const Info = () => {

const [state, setState] = useState({
  hint: '053...',
  value:'',
  output:'',
  hidden: true,
});

const [isOpen, setOpen] = useState(false);

const handleChange = (e) => {
  if(e.target.value.length === 11 && (/^[0-9]+$/.test(e.target.value))){
    setState({value: e.target.value, hidden: true});
    console.log(e.target.value + ' is a valid phone number.');
  } else {
    setState({value: 'Invalid phone number', hidden: false});
    console.log('Invalid phone number');
  }
}

const confirm = () => {
  if((state.value === 'Invalid phone number' && state.hidden === false) || state.value === ''){
    setState({output: 'Hata: Geçerli bir telefon numarası girin!'});
  }
  else {
    setState({output: state.value, hidden: true});
    //creating post request to the nodejs server
    axios.post('http://localhost:3001/api', {
      phone: state.value,
    })
    .then((response) => {
      console.log(response.data);
      console.log("🟠got status from landing api: "+response.data.status);
      if(response.data.status === 1){
        setState({output: 'Hata: Numaranız operatörde kayıtlı değil!', hidden: false});
      }
      else if(response.data.status === 3){
        setState({output: 'Hata: Bu servise kayıt olmak için yeterli bakiyeniz yok!', hidden: false});
      }
      else if(response.data.status === 0){
        setOpen(true); 
      }

    }, (error) => {
      console.log(error);
    });
  }
}

  return (
    <>
    <InfoSec>
        <Container>
        <InfoColumn>
          <ImgWrap>
            <Img src= {svg} alt='Image'>
            </Img>
          </ImgWrap>
        </InfoColumn>
        <InfoColumn> 
          <StyledTextArea placeholder={state.hint} onChange={(e) => handleChange(e)}>
          </StyledTextArea>
        </InfoColumn>
        <InfoColumn> 
          <Button onClick={() => confirm()}>
            Onayla
          </Button>
          <Modal isOpen={isOpen} close = {() => {setOpen(false); window.location.reload(true)} }>
            <TextWrap>
              <h2>İşlem Başarılı!</h2>
            </TextWrap>
          </Modal>
        </InfoColumn>
        <InfoColumn>
          <ErrorWrap>
            {state.hidden ? null : <p>{state.output}</p>}
          </ErrorWrap>
        </InfoColumn>
        <InfoColumn>
            <TextWrap>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            </TextWrap>
        </InfoColumn>
        </Container> 
    </InfoSec>
    </>
  )
}

export default Info;