function reformat(data){
	var cards = data.cards;
	var freeFor = {};
	for(var i=0; i<cards.length; ++i){
		var card = cards[i];
		
		//reformat effects
		var effects = card.effects;
		var baseAssets = {};
		var multiplierAssets = [];
		var multiplierTargets = [];
		var discountsAssets = [];
		var discountsTargets = [];
		var isChoice = false;
		for(var j=0; j<effects.length; ++j){
			var effect = effects[j];
			var type = effect.type;
			if(type.indexOf("choice")!=-1){
				//it's a choice
				isChoice = true;
				var list = effect.list;
				for(var k=0; k<list.length; ++k){
					baseAssets[list[k]] = 1;
				}
			}else if(type.indexOf("cost")!=-1){
				//it's a price reducer
				
				//what does it reduce
				if(type.charAt(0)=="m"){
					discountsAssets = ["g","p","l"];
				}else if(type.charAt(0)=="r"){
					discountsAssets = ["c","o","w","s"];
				}
				
				//who does it reduce
				if(type.indexOf("East")>-1){
					discountsTargets.push("right");
				}else if(type.indexOf("West")>-1){
					discountsTargets.push("left");
				}
			}else{
				//generic asset
				baseAssets[type] = effect.amount || 1;
			}		
			var from = effect.from;
			var basis = effect.basis;
			if(from && basis && basis!="none"){
				if(from=="all"){
					multiplierTargets = ["left", "self", "right"];
				}else if(from=="player"){
					multiplerTargets = ["self"];
				}else if(from=="neighbors"){
					multiplierTargets = ["left", "right"];
				}
				multiplierAssets.push(basis);
			}				
		}
		
		delete card.effects;
		
		card.baseAssets = baseAssets;
		if(multiplierAssets.length) card.multiplierAssets = multiplierAssets;
		if(multiplierTargets.length) card.multiplierTargets = multiplierTargets;
		if(discountsAssets.length) card.discountsAssets = discountsAssets;
		if(discountsTargets.length) card.discountsTargets = discountsTargets;
		if(isChoice) card.isChoice = isChoice; 
		
		//reformat chains to freeFor pt. 1
		if(card.chains){
			var chains = card.chains;
			for(var j=0; j<chains.length; ++j){
				freeFor[chains[i]] = card.name;
			}
		}
		delete card.chains;
	}
	
	//reformat chains to freeFor pt. 2
	for(var i=0; i<cards.length; ++i){
		var card = cards[i];
		if(freeFor[card.name]){
			card.freeFor = freeFor[card.name];
		}
	} 	
	
	echo(data);
}

function echo(data){
	console.log(JSON.stringify(data));
}

$.getJSON("target.json", reformat);
